package nqjtooling.transpiler.frontend;

import nqjtooling.transpiler.minijava.ast.*;

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
}
