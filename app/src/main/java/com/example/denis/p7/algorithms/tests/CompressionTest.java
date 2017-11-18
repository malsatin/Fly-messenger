package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.compression.Huffman;
import com.example.denis.p7.algorithms.compression.LZ77;
import com.example.denis.p7.algorithms.compression.RLE;
import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.ByteHelper;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

/**
 * Example of LZ77 algorithm usage.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-07
 */
public class CompressionTest {

    public static void main(String[] args) throws DecompressionException {
        String testStr = "<<<<<<<<<<<<<<<<<<<<<<<<<Бебе бубу ()?* 1233. In each of the types of compression schemes the goal is to compress down to the entropy of the source.";

        ICompressor[] algorithms = {new Huffman(), new RLE(), new LZ77()};

        byte[] in = ByteHelper.getBytesFromString(testStr);

        for(ICompressor algo : algorithms) {
            String str2;
            System.out.println(algo.getClass().getSimpleName() + ":\n");

            byte[] input = in.clone();
            byte[] comp = algo.compressByteString(input);

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

            System.out.println("Before compression: " + testStr);
            System.out.println("After compression:  " + str2);
            System.out.println("Initial size: " + in.length);
            System.out.println("Compressed size: " + comp.length);
            System.out.println("Decompressed size: " + res.length);

            System.out.println("Compress ratio: " + String.format("%.2f", ratio));
            System.out.println("\n----------------------------\n");
        }
    }

}
