package nqjtooling.transpiler.frontend;

import nqjtooling.transpiler.minijava.ast.*;

public class AstPrinter implements MJElement.Visitor {
    private StringBuilder out = new StringBuilder();
    private int indent = 0;

    public static String print(MJElement ast) {
        if (ast == null) {
            return "<null>";
        }
        AstPrinter printer = new AstPrinter();
        ast.accept(printer);
        return printer.out.toString();
    }

    private void println() {
        out.append("\n");
        for (int i = 0; i < indent; i++) {
            out.append("    ");
        }
    }

    private void print(String s) {
        out.append(s);
    }

    private void println(String s) {
        out.append(s);
        println();
    }

    @Override
    public void visit(MJNewArray na) {
        print("(new ");
        na.getBaseT().accept(this);
        for (MJExpr e : na.getArraySizes()) {
            print("[");
            e.accept(this);
            print("]");
        }
        for (int i = 0; i < na.getDimensions(); i++) {
            print("[]");
        }
        print(")");
    }

    @Override
    public void visit(MJExprNull exprNull) {
        print("null");
    }

    @Override
    public void visit(MJStmtAssign stmtAssign) {
        stmtAssign.getAddress().accept(this);
        print(" = ");
        stmtAssign.getRight().accept(this);
        println(";");
    }

    @Override
    public void visit(MJLess less) {
        print("<");
    }

    @Override
    public void visit(MJVarDeclList varDeclList) {
        for (MJVarDecl v : varDeclList) {
            v.accept(this);
            println(";");
        }
    }

    @Override
    public void visit(MJExtendsNothing extendsNothing) {

    }

    @Override
    public void visit(MJVarUse varUse) {
        print(varUse.getVarName());
    }

    @Override
    public void visit(MJTopLevelDeclList topLevelDeclList) {
        for (MJTopLevelDecl decl : topLevelDeclList) {
            decl.accept(this);
            println();
        }
    }

    @Override
    public void visit(MJRead read) {
        read.getAddress().accept(this);
    }

    @Override
    public void visit(MJExprList exprList) {
        for (MJExpr expr : exprList) {
            if (expr != exprList.get(0)) {
                print(", ");
            }
            expr.accept(this);
        }

    }

    @Override
    public void visit(MJInterfaceMemberDeclList interfaceMemberDeclList) {

    }

    @Override
    public void visit(MJInterfaceFunctionDeclList interfaceFunctionDeclList) {
        for (MJInterfaceFunctionDecl m : interfaceFunctionDeclList) {
            m.accept(this);
            println();
        }
    }

    @Override
    public void visit(MJInterfaceDeclList interfaceDeclList) {

    }

    @Override
    public void visit(MJMemberDeclList memberDeclList) {
        for (MJMemberDecl member : memberDeclList) {
            member.accept(this);
        }
    }

    @Override
    public void visit(MJNegate negate) {
        print("!");
    }

    @Override
    public void visit(MJModifierList modifierList) {
        for (MJModifier mod : modifierList) {
            mod.accept(this);
            print(" ");
        }
    }

    @Override
    public void visit(MJStmtWhile stmtWhile) {
        print("while (");
        stmtWhile.getCondition().accept(this);
        print(") ");
        stmtWhile.getLoopBody().accept(this);
    }

    @Override
    public void visit(MJExtendsClass extendsClass) {
        print("extends ");
        print(extendsClass.getName());
        print(" ");
    }

    @Override
    public void visit(MJImplementsNothing implementsNothing) {

    }

    @Override
    public void visit(MJImplementsClass implementsClass) {
        print("implements ");
        print(implementsClass.getName());
        print(" ");
    }

    @Override
    public void visit(MJModifierPublic modifierPublic) {
        print("public");
    }

    @Override
    public void visit(MJModifierProtected modifierProtected) {
        print("protected");
    }

    @Override
    public void visit(MJModifierPrivate modifierPrivate) {
        print("private");
    }

    @Override
    public void visit(MJModifierStatic modifierStatic) {
        print("static");
    }

    @Override
    public void visit(MJModifierFinal modifierFinal) {
        print("final");
    }

    @Override
    public void visit(MJModifierNative modifierNative) {
        print("native");
    }

    @Override
    public void visit(MJArrayLookup arrayLookup) {
        arrayLookup.getArrayExpr().accept(this);
        print("[");
        arrayLookup.getArrayIndex().accept(this);
        print("]");
    }

    @Override
    public void visit(MJMethodCall methodCall) {
        methodCall.getReceiver().accept(this);
        print(".");
        print(methodCall.getMethodName());
        print("(");
        methodCall.getArguments().accept(this);
        print(")");
    }

    @Override
    public void visit(MJFunctionCall functionCall) {
        print(functionCall.getMethodName());
        print("(");
        functionCall.getArguments().accept(this);
        print(")");
    }

    @Override
    public void visit(MJTypeClass typeClass) {
        print(typeClass.getName());
    }

    @Override
    public void visit(MJNewObject newObject) {
        print("new ");
        print(newObject.getClassName());
        print("()");
    }

    @Override
    public void visit(MJTimes times) {
        print("*");
    }

    @Override
    public void visit(MJMinus minus) {
        print("-");
    }

    @Override
    public void visit(MJExprUnary exprUnary) {
        print("(");
        exprUnary.getUnaryOperator().accept(this);
        print(" ");
        exprUnary.getExpr().accept(this);
        print(")");
    }

    @Override
    public void visit(MJStmtReturn stmtReturn) {
        print("return ");
        stmtReturn.getResult().accept(this);
        println(";");
    }

    @Override
    public void visit(MJProgram program) {
        program.getTopLevelDecls().accept(this);
    }

    @Override
    public void visit(MJEquals equals) {
        print("==");
    }

    @Override
    public void visit(MJUnaryMinus unaryMinus) {
        print("-");
    }

    @Override
    public void visit(MJExprThis exprThis) {
        print("this");
    }

    @Override
    public void visit(MJVarDecl varDecl) {
        varDecl.getType().accept(this);
        print(" ");
        print(varDecl.getName());
    }

    @Override
    public void visit(MJFieldAccess fieldAccess) {
        fieldAccess.getReceiver().accept(this);
        print(".");
        print(fieldAccess.getFieldName());
    }

    @Override
    public void visit(MJNumber number) {
        print("" + number.getIntValue());
    }

    @Override
    public void visit(MJTypeInt typeInt) {
        print("int");
    }

    @Override
    public void visit(MJTypeVoid typeVoid) {
        print("void");
    }

    @Override
    public void visit(MJMethodDeclList methodDeclList) {
        for (MJMethodDecl m : methodDeclList) {
            m.accept(this);
            println();
        }
    }

    @Override
    public void visit(MJPlus plus) {
        print("+");
    }

    @Override
    public void visit(MJExprBinary exprBinary) {
        print("(");
        exprBinary.getLeft().accept(this);
        print(" ");
        exprBinary.getOperator().accept(this);
        print(" ");
        exprBinary.getRight().accept(this);
        print(")");
    }

    @Override
    public void visit(MJBlock block) {
        indent++;
        println("{");
        for (MJStatement s : block) {
            s.accept(this);
            if (s instanceof MJVarDecl) {
                println(";");
            }
        }
        indent--;
        println();
        println("}");

    }

    @Override
    public void visit(MJDiv div) {
        print("/");
    }

    @Override
    public void visit(MJBoolConst boolConst) {
        print("" + boolConst.getBoolValue());
    }

    @Override
    public void visit(MJStmtIf stmtIf) {
        print("if (");
        stmtIf.getCondition().accept(this);
        print(") ");
        stmtIf.getIfTrue().accept(this);
        print("else ");
        stmtIf.getIfFalse().accept(this);

    }

    @Override
    public void visit(MJTypeBool typeBool) {
        print("boolean");
    }

    @Override
    public void visit(MJAnd and) {
        print("&&");
    }

    @Override
    public void visit(MJStmtExpr stmtExpr) {
        stmtExpr.getExpr().accept(this);
        println(";");
    }

    @Override
    public void visit(MJMethodDecl methodDecl) {
        print("public ");
//        methodDecl.getModifiers().accept(this);
        methodDecl.getReturnType().accept(this);
        print(" ");
        print(methodDecl.getName());
        print("(");
        for (MJVarDecl p : methodDecl.getFormalParameters()) {
            if (p != methodDecl.getFormalParameters().get(0)) {
                print(", ");
            }
            p.accept(this);
        }
        print(") ");
        methodDecl.getMethodBody().accept(this);
    }

    @Override
    public void visit(MJInterfaceFunctionDecl interfaceFunctionDecl) {
        interfaceFunctionDecl.getReturnType().accept(this);
        print(" ");
        print(interfaceFunctionDecl.getName());
        print("(");
        for (MJVarDecl p : interfaceFunctionDecl.getFormalParameters()) {
            if (p != interfaceFunctionDecl.getFormalParameters().get(0)) {
                print(", ");
            }
            p.accept(this);
        }
        print("); ");
    }

    @Override
    public void visit(MJTypeArray typeArray) {
        typeArray.getComponentType().accept(this);
        print("[]");
    }

    @Override
    public void visit(MJClassDecl classDecl) {
        print("class ");
        print(classDecl.getName());
        print(" ");
        classDecl.getExtended().accept(this);
        print(" ");
        classDecl.getImpls().accept(this);
        indent++;
        println(" {");
        for (MJVarDecl v : classDecl.getFields()) {
            v.accept(this);
            println(";");
        }
        classDecl.getMethods().accept(this);
        indent--;
        println("}");

    }

    @Override
    public void visit(MJInterfaceDecl interfaceDecl) {
        print("interface ");
        print(interfaceDecl.getName());
        print(" ");
        indent++;
        println(" {");
        interfaceDecl.getInterfaceMethods().accept(this);
        indent--;
        println("}");
    }

    @Override
    public void visit(MJReturnNothing nothing) {
    }

    @Override
    public void visit(MJReturnExpr expr) {
        expr.getExpr().accept(this);
    }
}
