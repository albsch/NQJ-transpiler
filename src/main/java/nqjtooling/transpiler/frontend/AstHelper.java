package nqjtooling.transpiler.frontend;


import nqjtooling.transpiler.minijava.ast.*;

import java.util.List;


/**
 * Helper methods to be used inside CUP grammar rules.
 */
public class AstHelper {
    /**
     * Parsing members of classes into a class declaration.
     */
    public static MJClassDecl classDecl(String name, String ext, List<MJMemberDecl> members) {
        MJFunctionDeclList methods = MJ.FunctionDeclList();
        MJVarDeclList fields = MJ.VarDeclList();
        MJExtended extended;
        if (ext == null) {
            extended = MJ.ExtendsNothing();
        } else {
            extended = MJ.ExtendsClass(ext);
        }

        for (MJMemberDecl member : members) {
            member.match(new MJMemberDecl.MatcherVoid() {

                @Override
                public void case_FunctionDecl(MJFunctionDecl methodDecl) {
                    methods.add(methodDecl.copy());
                }

                @Override
                public void case_VarDecl(MJVarDecl varDecl) {
                    fields.add(varDecl.copy());
                }
            });
        }

        return MJ.ClassDecl(name, extended, fields, methods);
    }

    /** Parsing top level delcaration into a program. */
    public static MJProgram program(List<MJTopLevelDecl> decls) {
        MJFunctionDeclList functions = MJ.FunctionDeclList();
        MJClassDeclList classDecls = MJ.ClassDeclList();

        for (MJTopLevelDecl decl : decls) {
            decl.match(new MJTopLevelDecl.MatcherVoid() {
                @Override
                public void case_FunctionDecl(MJFunctionDecl functionDecl) {
                    functions.add(functionDecl.copy());
                }

                @Override
                public void case_ClassDecl(MJClassDecl classDecl) {
                    classDecls.add(classDecl.copy());
                }
            });
        }

        return MJ.Program(classDecls, functions);
    }

    public static MJType BuildArrayType(MJType t, int dimensions) {
        for (int i = 0; i < dimensions; i++) {
            t = MJ.TypeArray(t);
        }
        return t;
    }

    public static MJType BuildArrayType(MJExprL e, int dimensions) {
        MJType t;
        if (e instanceof MJVarUse) {
            MJVarUse vu = (MJVarUse) e;
            t = MJ.TypeClass(vu.getVarName());
        } else {
            t = MJ.TypeClass("unknown type");
        }
        for (int i = 0; i < dimensions; i++) {
            t = MJ.TypeArray(t);
        }
        return t;
    }

    public static MJExpr NewArray(MJType t, MJExpr size, int dimensions) {
        MJType arrayType = t.copy();
        for (int i = 0; i < dimensions; i++) {
            arrayType = MJ.TypeArray(arrayType);
        }
        return MJ.NewArray(arrayType, size, t, dimensions);
    }
}

