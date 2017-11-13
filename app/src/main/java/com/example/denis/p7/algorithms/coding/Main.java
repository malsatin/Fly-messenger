//package com.example.denis.p7.algorithms.coding;
//
//import com.example.denis.p7.algorithms.exceptions.DecodingException;
//import com.example.denis.p7.algorithms.helpers.ByteHelper;
//import com.example.denis.p7.algorithms.helpers.NoiseHelper;
//import com.example.denis.p7.algorithms.interfaces.ICoder;
//
//import java.io.IOException;
//
//public class Main {
//    private static final double BIT_FLIP_PROB = 0.005;
//    private static final String IN_FILE_NAME = "input.txt";
//
//    public static void main(String[] args) throws IOException {
//        byte[] file = ByteHelper.readBytesFromFile(IN_FILE_NAME);
//        ICoder[] algorithms = {new HammingCode(), new ParityBit(), new RepetitionCode()};
//        for (ICoder alg : algorithms) {
//            byte[] code = alg.encodeByteString(file);
//            NoiseHelper.applyNoise(code, BIT_FLIP_PROB);
//            try {
//                ByteHelper.writeBytesToFile(alg.decodeByteString(code),
//                        alg.getClass().getSimpleName() + "_" + IN_FILE_NAME);
////                System.out.println(ByteHelper.getStringFromBytes(alg.decodeByteString(code)) + '\n');
//            } catch (DecodingException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
