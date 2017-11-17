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
     * Shows, how many bytes can there be in a single sequence (due optimization there actually can be 1 more byte)
     * Optimal to be a (power of 2) - 1. About 1-3 is a good variant
     */
    private final int FRAME_SIZE = 1;

    private int lengthBitSize; // Count of bits to represent length part

    public RLE() {
        lengthBitSize = convertToBitCount(FRAME_SIZE);
    }

    @Override
    public byte[] compressByteString(byte[] message) {
        BitStream inStream = new BitStream(message);
        BitStream outStream = new BitStream();

        while(inStream.hasBits()) {
            byte curByte = inStream.readByte();
            int curCount = 0;
            while(inStream.hasBits() && curCount < FRAME_SIZE) {
                if(inStream.readByte() != curByte) {
                    inStream.revert(BitStream.BYTE_SIZE);
                    break;
                }
                curCount++;
            }

            outStream.addByte(curByte);
            outStream.addNumber(curCount, lengthBitSize);
        }

        return outStream.toByteArray();
    }

    @Override
    public byte[] decompressByteString(byte[] sequence) throws DecompressionException {
        BitStream inStream = new BitStream(sequence);
        BitStream outStream = new BitStream();

        while(inStream.hasBits()) {
            if(inStream.bitsRemain() < BitStream.BYTE_SIZE + lengthBitSize) {
                if(inStream.bitsRemain() < BitStream.BYTE_SIZE) {
                    break;
                }

                throw new DecompressionException("There is not enough bits to parse");
            }

            byte curByte = inStream.readByte();
            int curCount = (int)inStream.readNumber(lengthBitSize) + 1;
            for(int i = 0; i < curCount; i++) {
                outStream.addByte(curByte);
            }
        }

        return outStream.toByteArray();
    }

    private static int convertToBitCount(int length) {
        return (int)(Math.log(length) / Math.log(2)) + 1;
    }

}
