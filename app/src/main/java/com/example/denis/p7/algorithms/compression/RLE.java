package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

/**
 * RLE compression algorithm
 * <p>
 * Created by Sergey on 2017-11-07
 */
public class RLE implements ICompressor {

    /**
     * Shows, how many bytes can there be in a single sequence
     * Optimal to be a (power of 2) - 1. About 7-15 is a good variant
     */
    private final int FRAME_SIZE = 7;

    @Override
    public byte[] compressByteString(byte[] message) {
        BitStream inStream = new BitStream(message);
        BitStream outStream = new BitStream();

        // TODO: implement algorithm

        return outStream.toByteArray();
    }

    @Override
    public byte[] decompressByteString(byte[] sequence) throws DecompressionException {
        BitStream inStream = new BitStream(sequence);
        BitStream outStream = new BitStream();

        // TODO: implement algorithm

        return outStream.toByteArray();
    }

}
