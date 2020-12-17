package nqjtooling.transpiler.frontend;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Miscellaneous file utilities
 */
public class FileUtils {
    /**
     * Read the remaining content of the reader into a string.
     *
     * @param reader The reader to finish.
     * @return The remaining content of the reader as string.
     * @throws IOException
     */
    public static String readToEnd(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append('\n');
        }

        return buffer.toString();
    }

    /**
     * Read the contents of a file into a string. Assumes a UTF-8 encoded file, for files using a different encoding
     * {@link #readFile(File, String)}
     *
     * @param file The file to read.
     * @return The content of the file as string.
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        return readFile(file, "UTF-8");
    }

    /**
     * Read the contents of a file into a string, using the specified encoding.
     *
     * @param file The file to read.
     * @param encoding The encoding of the file.
     * @return The content of the file as string.
     * @throws IOException
     */
    public static String readFile(File file, String encoding) throws IOException {
        Charset charset = Charset.forName(encoding);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            return readToEnd(reader);
        }
    }
}
