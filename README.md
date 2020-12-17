![Java CI](https://github.com/albsch/NQJ-transpiler/workflows/Java%20CI/badge.svg)

# Translating the AST to LLVM
The program is translated by iterating over all classes defined in the program, and for each class:

* construct a procedure for each defined method
* construct a global for each defined static variable

There are some problems which we need to resolve.

## Name Uniqueness
Procedures and globals must have a globally unique name. However, two methods or static fields defined
in separate classes may share the same name. As such, we use *name mangling* to encode the class name into the 
name of the llvm element in a style similar to C++.

A mangled name looks like this:
`_Z<type>N<length prefixed class name><length prefixed member name>E<parameter list>`

where
* `_Z` denotes the start of an identifier
* `<type>` denotes the type of element referenced by the name: `P` for a procedure or `G` for a global
* `N` denotes a namespaced identifier
* `E` denotes the end of a namespaced identifier
* `<length prefixed name>` is `<length><name>`
* `<parameter list>` is empty for globals. For procedures, it contains the length-prefixed names of the types of 
the arguments. This permits us to optionally implement overloading later on. Booleans are encoded as `b` and ints
are encoded `i`

Examples:
* The static field `System.out` is mangled as `_ZGN6System3outE`
* The method `OutStream::println(int value)` is mangled as `_ZPN9OutStream7printlnEi`

## Cyclic Dependencies While Translating Methods
When we translate method calls, we need a `ProcRef` to the target `Procedure`. However, this `Proceudre` may not have 
been constructed yet. In particular, if we were to translate this target method first, this could be blocked by a call 
to the first method.

As such, we split the procedure construction into two phases: First, we only construct the skeleton of a procedure:
The mangled procedure name and the list of parameters but no associated basic blocks.

In the second pass we then translate the method bodies where we can reference the `Procedures` from the first
pass.

## Assigning to Variables and Parameters
The only way to permanently store information in a variable is via the `store` instruction. As such, we translate
variable declarations by allocating stack space, giving us a pointer into the stack that can be used with `store`.

In order to handle variables and parameters uniformly, at the entry point of a procedure, we allocate stack space
for each parameter and store the parameter there.

## Translating Method Bodies
The translation process keeps track of what basic blocks have been constructed and what variables are required.
Both the statement and expression translators keep track of the first and last block that are required to translate
the specific expression or statement so the blocks for the various statements can be joined.
New statements are translated one by one and generally appended to the current block. Some special cases exist.

Scopes (that are not used as part of a control-flow statement) are simply ignored, since they have been handled during
the analysis phase.

For if statements the true and false branch are constructed independently and recursively. A branch operation
targeting the first block of each of these branches is added to the current block. A new join block form the next
block currently being constructed and the two branches are linked to this block.

For while statements, a new basic block containing only the loop condition and the branch operation is created. The
branch targets the loop body, again constructed recursively.

Short circuiting is implemented for the operators `&&` and `||` and uses phi-nodes to obtain the final result so it 
need not be stored onto the stack. If the left side evaluates to false or true respectively, then the right side is not
evaluated and just the result of the left side is used.

## Further Details
Non-static methods get passed an implicit first parameter, a reference to the receiver object. This corresponds
to, and is used to implement, the `this` keyword.

Void methods are called with `CallVoid` instead of `Call` as LLVM demands that no variable assignment takes place
for void calls.

When translating a field access, first it is checked if the static type of the receiver expression has a static
member with that name. If yes, that field is accessed. Otherwise, a field access on the struct resulting from
the receiver expression is emitted. This matches standard Java behavior.

When translating variable uses, the corresponding declaration of that variable is checked. If the declaration is 
null, this corresponds to a class name used as part of a static field access. This doesn't need to be translated.
If the declaration has no source object, it is a local variable. Otherwise an implicit field access or global 
access is emitted, based on the `isStatic` property of the declaration.

To initialize all values (including fields and array members) to their default value, we exploit that all default values
are represented by null bytes and simply replace `malloc` by `calloc`.
