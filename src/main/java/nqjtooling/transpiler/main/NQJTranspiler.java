package nqjtooling.transpiler.main;

import nqjtooling.transpiler.frontend.AstPrinter;
import nqjtooling.transpiler.frontend.FileUtils;
import nqjtooling.transpiler.frontend.MJFrontend;
import nqjtooling.transpiler.frontend.SyntaxError;
import nqjtooling.transpiler.minijava.ast.MJProgram;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class NQJTranspiler {
    private MJProgram javaProgram;
    private MJFrontend frontend;

    public static void main(String[] args) throws Exception {
        String fileName;
        if (args.length > 0) {
            fileName = args[0];
        } else {
            System.out.println("Enter a filename: ");
            fileName = new Scanner(System.in).nextLine();
        }
        NQJTranspiler compiler = new NQJTranspiler();
        File inputFile = new File(fileName);
        compiler.compileFile(inputFile);

        if (!compiler.getSyntaxErrors().isEmpty()) {
            compiler.getSyntaxErrors().forEach(System.out::println);
            System.exit(7);
        }
        compiler.print();
    }

    public void compileFile(File file) throws Exception {
        try (FileReader r = new FileReader(file)) {
            compile(file.getPath(), FileUtils.readToEnd(new BufferedReader(r)));
        }
    }

    public void compile(String inputName, String inputStr) throws Exception {
        frontend = new MJFrontend();

        javaProgram = frontend.parseString(inputStr);
        frontend.getSyntaxErrors().forEach(f -> f.report(inputName, inputStr));
        if (!frontend.getSyntaxErrors().isEmpty()) {
            return;
        }
    }

    public static final String globalClassName = "___Global";

    public String print() {
        String prefix = "import java.lang.System; class " + globalClassName + " { static int printInt(int value) { System.out.println(value); return value; } public static void main(String[] args) { System.exit(new "
                +globalClassName +"().main()); } \n";
        String postfix = "}\n";
        return prefix + AstPrinter.print(javaProgram) + postfix;
    }

    public static String transform(String input) throws Exception {
        NQJTranspiler compiler = new NQJTranspiler();
        compiler.compile("nqj", input);
        return compiler.print();
    }

    public List<SyntaxError> getSyntaxErrors() {
        return frontend.getSyntaxErrors();
    }
}
