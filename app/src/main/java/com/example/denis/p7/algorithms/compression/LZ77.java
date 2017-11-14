package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;
import org.apache.commons.compress.utils.BitInputStream;

/**
 * LZ77 compression algorithm
 * <p>
 * Created by Sergey on 2017-11-07
 */
public class LZ77 implements ICompressor {

    /**
     * Shows, how many bytes we can look back to find such sequence
     * Optimal to be a multiple of 2
     */
    private final int WINDOW_SIZE = 16;
    /**
     * Shows, how many bytes can there be in a single sequence
     * Optimal to be a multiple of 2
     */
    private final int FRAME_SIZE = 4;

    private int lookbackBitSize;
    private int lengthBitSize;

    public LZ77() {
        lookbackBitSize = convertToBitCount(WINDOW_SIZE);
        lengthBitSize = convertToBitCount(FRAME_SIZE);
    }

    @Override
    public byte[] compressByteString(byte[] message) {
        BitStream input = new BitStream(message);
        BitStream output = new BitStream();

        while(input.hasBits()) {
            byte currentByte = input.readByte();

            output.addByte(currentByte);
        }

        // TODO: implement algorithm
        return output.toByteArray();
    }

    @Override
    public byte[] decompressByteString(byte[] sequence) throws DecompressionException {
        // TODO: implement algorithm
        return sequence.clone();
    }

    private int convertToBitCount(int length) {
        return (int)(Math.log(length) / Math.log(2)) + 1;
    }

}
