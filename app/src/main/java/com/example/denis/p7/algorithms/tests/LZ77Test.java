package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.compression.LZ77;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.helpers.ByteHelper;

import java.util.Arrays;

/**
 * Example of LZ77 algorithm usage.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-07
 */
public class LZ77Test {

    public static void main(String[] args) {
        LZ77 algo = new LZ77();

        byte[] in = ByteHelper.getBytesFromString("abaab");
        byte[] out = algo.compressByteString(in);

        System.out.println(LZ77.debugOutput(out));
        System.out.println(new BitStream(out).toString());
        System.out.println(Arrays.toString(out) + " " + ByteHelper.getStringFromBytes(out));
        System.out.println("Input size: " + in.length + "; output size: " + out.length);
    }

}
