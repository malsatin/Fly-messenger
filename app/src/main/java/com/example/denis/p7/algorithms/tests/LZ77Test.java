package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.compression.LZ77;
import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.ByteHelper;

/**
 * Example of LZ77 algorithm usage.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-07
 */
public class LZ77Test {

    public static void main(String[] args) throws DecompressionException {
        String str1 = "Бебе бубу ()?* 1233. In each of the types of compression schemes the goal is to compress down to the entropy of the source.";
        String str2;
        LZ77 algo = new LZ77();

        byte[] in = ByteHelper.getBytesFromString(str1);

        /*System.out.println("Compression:\n");*/

        byte[] comp = algo.compressByteString(in);

        /*System.out.println(algo.debugOutput(out));
        System.out.println(new BitStream(out).toString());
        System.out.println(Arrays.toString(out) + " " + ByteHelper.getStringFromBytes(out));
        System.out.println("Input size: " + in.length + "; output size: " + out.length);*/

        /*System.out.println("\nDecompression:\n");*/

        byte[] res = algo.decompressByteString(comp);
        str2 = ByteHelper.getStringFromBytes(res);

        /*System.out.println(new BitStream(res).toString());
        System.out.println(Arrays.toString(res) + " " + ByteHelper.getStringFromBytes(res));
        System.out.println("Input size: " + out.length + "; output size: " + res.length);*/

        double ratio = (double)comp.length / in.length;

        System.out.println("Before compression: " + str1);
        System.out.println("After compression:  " + str2);
        System.out.println("Initial size: " + in.length);
        System.out.println("Compressed size: " + comp.length);
        System.out.println("Decompressed size: " + res.length);

        System.out.println("Compress ratio: " + String.format("%.2f", ratio));
    }

}
