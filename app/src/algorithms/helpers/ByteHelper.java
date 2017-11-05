package algorithms.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class represents some operations with files as sequences of bytes.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-05
 */
public class ByteHelper {
    /**
     * Parse a unicode ({@code UTF-8}) string into the sequence of bytes.
     *
     * @param str String to convert into an array of bytes
     * @return Sequence of bytes representing this string
     */
    public static byte[] getBytesFromString(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * TODO
     *
     * @param sequence
     * @return
     */
    public static String getStringFromBytes(byte[] sequence) {
        try {
            return new String(sequence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeBytesToFile(byte[] sequence, String path) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(path);
        int bufferSize = 1024;
        int i, j;
        for (i = 0; i < sequence.length; i += bufferSize) {
            for (j = 0; j < bufferSize; ++j) {
                pw.print(sequence[i + j]);
            }
            pw.flush();
        }
    }
}
