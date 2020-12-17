package nqjtooling.transpiler.frontend;

import nqjtooling.transpiler.minijava.ast.MJProgram;

import java.io.File;
import java.io.StringReader;
import java.util.Scanner;

/**
 * Entry Point.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String fileName;
        if (args.length > 0) {
            fileName = args[0];
        } else {
            System.out.println("Enter a filename: ");
            fileName = new Scanner(System.in).nextLine();
        }
        String input = FileUtils.readFile(new File(fileName));
        MJFrontend frontend = new MJFrontend();
        MJProgram prog = frontend.parseString(input);
        System.out.println(prog);

        frontend.getSyntaxErrors().forEach(err -> err.report(fileName, input));
    }

    public static MJProgram parseToAST(String input) throws Exception {
        MJFrontend parser = new MJFrontend();
        return parser.parse(new StringReader(input));
    }

}
