package nqjtooling.transpiler.frontend;

import nqjtooling.transpiler.minijava.ast.*;

import java.util.List;

/**
 * Miscellaneous methods to keep the cup file clean.
 */
public class ParserUtils {

    /**
     * Extracts all member declarations that are field declarations.
     * @param members   the list of all members of a class
     * @return          only the variable declarations from {@code members}
     */
    public static MJVarDeclList extractVarDeclarationsFromMembers(MJMemberDeclList members) {
        MJVarDeclList result = MJ.VarDeclList();
        for (MJMemberDecl member : members) {
            if (member instanceof MJVarDecl) {
                MJVarDecl copy = ((MJVarDecl) member).copy();
                copy.setSourcePosition(member.getSourcePosition());
                result.add(copy);
            }
        }
        return result;
    }

    /**
     * Extracts all member declarations that are method declarations.
     * @param members   the list of all members of a class
     * @return          only the method declarations from {@code members}
     */
    public static MJMethodDeclList extractMethodDeclarationsFromMembers(MJMemberDeclList members) {
        MJMethodDeclList result = MJ.MethodDeclList();
        for (MJMemberDecl member : members) {
            if (member instanceof MJMethodDecl) {
                MJMethodDecl copy = ((MJMethodDecl) member).copy();
                copy.setSourcePosition(member.getSourcePosition());
                result.add(copy);
            }
        }
        return result;
    }

    public static MJInterfaceDecl interfaceDecl(String name, List<MJInterfaceFunctionDecl> members) {
        MJInterfaceFunctionDeclList mems = MJ.InterfaceFunctionDeclList();
        for (MJInterfaceFunctionDecl member : members) {
            mems.add(member.copy());
        }
        return MJ.InterfaceDecl(name, mems);
    }
}
