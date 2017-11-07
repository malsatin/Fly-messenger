package algorithms.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This class represents some operations with files as sequences of bytes.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-05
 * <p>
 * Edited by Denis Chernikov on 2017-11-05
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
            e.printStackTrace(); // Unreachable code
            return null;
        }
    }

    /**
     * Get the string out of the specified sequence of bytes ({@code UTF-8}).
     *
     * @param sequence Sequence of bytes to convert to string
     * @return String constructed out of this sequence of bytes
     */
    public static String getStringFromBytes(byte[] sequence) {
        try {
            return new String(sequence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Unreachable code
            return null;
        }
    }

    /**
     * Get the sequence of bytes out of the specified file.
     *
     * @param path Path to the source file
     * @return Sequence of bytes out of the specified file
     * @throws IOException File not found (wrong path)
     */
    public static byte[] readBytesFromFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * Write the sequence of bytes to the file specified by the path.
     * New file will be created; already existing file will be truncated to the length 0.
     *
     * @param sequence Sequence of bytes to write into file
     * @param path     Path to the destination file
     * @throws FileNotFoundException Path is wrong or some other error has occurred
     */
    public static void writeBytesToFile(byte[] sequence, String path) throws IOException {
        Files.write(Paths.get(path), sequence, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
}
