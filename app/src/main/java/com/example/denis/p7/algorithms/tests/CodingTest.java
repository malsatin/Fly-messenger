package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.coding.HammingCode;
import com.example.denis.p7.algorithms.coding.ParityBit;
import com.example.denis.p7.algorithms.coding.RepetitionCode;
import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.exceptions.FileTooBigException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.helpers.ByteHelper;
import com.example.denis.p7.algorithms.helpers.NoiseHelper;
import com.example.denis.p7.algorithms.interfaces.ICoder;

import java.io.IOException;
import java.util.Scanner;

public class CodingTest {
    private static final double BIT_FLIP_PROB = 0.01;
    private static final String IN_FILE_NAME = "input.txt";

    public static void main(String[] args) throws IOException, FileTooBigException {
//        byte[] file = ByteHelper.readBytesFromFile(IN_FILE_NAME);
        Scanner sc = new Scanner(System.in);
        byte[] file = sc.nextLine().getBytes();
        ICoder[] algorithms = {new HammingCode(), new ParityBit(), new RepetitionCode()};
        for (ICoder alg : algorithms) {
            BitStream code = alg.encodeByteString(file);
            NoiseHelper.applyNoise(code, BIT_FLIP_PROB);
            try {
//                ByteHelper.writeBytesToFile(alg.decodeBitStream(code).toByteArray(),
//                        alg.getClass().getSimpleName() + "_" + IN_FILE_NAME);
                System.out.println(alg.getClass().getSimpleName() + " `" +
                        ByteHelper.getStringFromBytes(alg.decodeBitStream(code).toByteArray()) + "`\n");
            } catch (DecodingException e) {
                e.printStackTrace();
            }
        }
    }
}
