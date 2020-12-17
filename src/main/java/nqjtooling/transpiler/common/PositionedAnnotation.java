package nqjtooling.transpiler.common;

import nqjtooling.transpiler.frontend.SourcePosition;
import nqjtooling.transpiler.minijava.ast.MJElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats the parsing errors to be presented to the user.<br>
 * To do this, the position causing the error in a snippet of the original program is marked and is assigned to the error message
 * error.
 */
public class PositionedAnnotation extends RuntimeException {
    private final String annotationType;
    private SourcePosition source;

    private List<PositionedAnnotation> supplementalInformation = new ArrayList<>();

    public PositionedAnnotation(String annotationType, String message, int line, int column) {
        super(message);
        this.source = new SourcePosition("", line, column, line, column);
        this.annotationType = annotationType;
    }

    public PositionedAnnotation(String annotationType, MJElement element, String message) {
        super(message);
        while (element != null) {
            this.source = element.getSourcePosition();
            if (this.source != null) {
                break;
            }
            element = element.getParent();
        }
        this.annotationType = annotationType;
    }

    public int getLine() {
        return source.getLine();
    }

    public int getColumn() {
        return source.getColumn();
    }

    public int getLength() {
        if (source.getLine() == source.getEndLine()) {
            return source.getEndColumn() - source.getColumn();
        }
        return 5;
    }

    public SourcePosition getSource() {
        return source;
    }

    public void addSupplementalInformation(PositionedAnnotation annotation) {
        this.supplementalInformation.add(annotation);
    }

    /**
     * Annotates the positions causing parsing errors in the original code.
     * @param sourceName The name of the file containing the program code.
     * @param input The program code which caused the parsing error.
     */
    public void report(String sourceName, String input) {
        String[] lines = input.split("\n");

        int line = getLine() - 1;
        int startLine = Math.max(getLine() - 2, 0);
        int endLine = Math.min(getLine() + 3, lines.length);
        // indent + sourcename + colon + line no + colon + space
        int pad = 2 + sourceName.length() + 1 + Integer.toString(line + 1).length() + 1 + 2;
        for (int i = startLine; i <= line; i++) {
            System.err.println("  " + sourceName + ":" + (i + 1) + ": " + lines[i]);
        }
        System.err.print('+');
        for (int _i = 1; _i < getColumn() + pad; _i++) {
            System.err.print('-');
        }
        System.err.println('^');
        for (int i = line + 1; i < endLine; i++) {
            System.err.println("| " + sourceName + ":" + (i + 1) + ": " + lines[i]);
        }
        System.err.println('|');
        System.err.println("+- " + annotationType + ": " + getMessage());
        System.err.println();

        for (PositionedAnnotation annotation: supplementalInformation) {
            annotation.report(sourceName, input);
        }
    }
}
