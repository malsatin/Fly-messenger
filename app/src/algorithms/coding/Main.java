package algorithms.coding;

import algorithms.exceptions.DecodingException;
import algorithms.helpers.ByteHelper;
import algorithms.helpers.NoiseHelper;
import algorithms.interfaces.ICoder;

import java.io.IOException;

public class Main {
    private static final double BIT_FLIP_PROB = 0.005;
    private static final String IN_FILE_NAME = "input.txt";

    public static void main(String[] args) throws IOException {
        byte[] file = ByteHelper.readBytesFromFile(IN_FILE_NAME);
        ICoder[] algorithms = {new HammingCode(), new ParityBit(), new RepetitionCode()};
        for (ICoder alg : algorithms) {
            byte[] code = alg.encodeByteString(file);
            NoiseHelper.applyNoise(code, BIT_FLIP_PROB);
            try {
                ByteHelper.writeBytesToFile(alg.decodeByteString(code),
                        alg.getClass().getSimpleName() + "_" + IN_FILE_NAME);
//                System.out.println(getStringFromBytes(alg.decodeByteString(code)) + '\n');
            } catch (DecodingException e) {
                e.printStackTrace();
            }
        }
    }
}
