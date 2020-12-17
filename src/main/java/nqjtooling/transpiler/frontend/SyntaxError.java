package nqjtooling.transpiler.frontend;

import nqjtooling.transpiler.common.PositionedAnnotation;
import nqjtooling.transpiler.minijava.ast.MJElement;

public class SyntaxError extends PositionedAnnotation {
    public SyntaxError(String message, int line, int column) {
        super("error", message, line, column);
    }

    public SyntaxError(MJElement element, String message) {
        super("error", element, message);
    }

    @Override
    public String toString() {
        return "Syntax error in line " + getLine() + ":" + getColumn() + ": " + getMessage();
    }

}