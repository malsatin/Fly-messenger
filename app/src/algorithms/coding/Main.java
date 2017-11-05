package algorithms.coding;

import algorithms.exceptions.DecodingException;
import algorithms.interfaces.ICoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static algorithms.helpers.NoiseHelper.applyNoise;

public class Main {
    private static final double BIT_FLIP_PROB = 0.005;
    private static final String IN_FILE_NAME = "input.txt";

    public static void main(String[] args) throws IOException {
        byte[] file = Files.readAllBytes(Paths.get(IN_FILE_NAME));
        ICoder[] algorithms = {new HammingCode(), new ParityBit(), new RepetitionCode()};
        for (ICoder alg : algorithms) {
            byte[] code = alg.encodeByteString(file);
            applyNoise(code, BIT_FLIP_PROB);
            try {
                Files.write(Paths.get(alg.getClass().getSimpleName() + "_" + IN_FILE_NAME),
                        alg.decodeByteString(code),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE);
//                System.out.println(new String(alg.decodeByteString(code)) + '\n');
            } catch (DecodingException e) {
                e.printStackTrace();
            }
        }
    }
}
