package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LZ77 compression algorithm
 * <p>
 * Created by Sergey on 2017-11-07
 */
public class LZ77 implements ICompressor {

    /**
     * Shows, how many bytes we can look back to find such sequence
     * Optimal to be a (power of 2) - 1. About 63-127 is a good variant
     */
    private static final int WINDOW_SIZE = 63;
    /**
     * Shows, how many bytes can there be in a single sequence
     * Optimal to be a (power of 2) - 1. About 7-15 is a good variant
     * FRAME_SIZE can be greater than WINDOW_SIZE
     */
    private static final int FRAME_SIZE = 7;

    private int lookbackBitSize; // Count of bits to represent lookback part
    private int lengthBitSize; // Count of bits to represent length part

    public LZ77() {
        lookbackBitSize = convertToBitCount(WINDOW_SIZE);
        lengthBitSize = convertToBitCount(FRAME_SIZE);
    }

    @Override
    public BitStream compressBitStream(BitStream inStream) {
        BitStream output = new BitStream();

        LinkedList<Byte> window = new LinkedList<>();
        ArrayList<Byte> frame;
        while(inStream.hasBits()) {
            int lastOccurrence = -1;
            int curOccurrence = 0;

            frame = new ArrayList<>(FRAME_SIZE);
            while(inStream.hasBits() && frame.size() < FRAME_SIZE) {
                frame.add(inStream.readByte());

                curOccurrence = findInWindow(window, frame);
                if(curOccurrence == -1) {
                    break;
                } else {
                    lastOccurrence = curOccurrence;
                }
            }

            if(lastOccurrence == -1) {
                output.addBit(0);
                output.addByte(frame.get(0));
            } else {
                if(curOccurrence == -1) {
                    frame.remove(frame.size() - 1);
                    inStream.revert(BitStream.BYTE_SIZE);
                }

                output.addBit(1);
                output.addNumber(window.size() - lastOccurrence, lookbackBitSize);
                output.addNumber(frame.size(), lengthBitSize);
            }

            window.addAll(frame);

            if(window.size() > WINDOW_SIZE) {
                window = new LinkedList<>(window.subList(window.size() - WINDOW_SIZE, window.size()));
            }
        }
        //System.out.println(output.toString());

        output.fillGap();
        output.reset();
        return output;
    }

    @Override
    public BitStream compressByteString(byte[] message) {
        return compressBitStream(new BitStream(message));
    }

    @Override
    public BitStream decompressBitStream(BitStream inStream) throws DecompressionException {
        BitStream output = new BitStream();

        LinkedList<Byte> window = new LinkedList<>();
        while(inStream.hasBits()) {
            boolean newByte = !inStream.readBit();
            byte[] bytesToAdd;

            if(newByte) {
                if(inStream.bitsRemain() < BitStream.BYTE_SIZE) {
                    break;  // Now remains zero information, so we are stopping
                }

                bytesToAdd = new byte[] {inStream.readByte()};
            } else {
                if(inStream.bitsRemain() < lookbackBitSize + lengthBitSize) {
                    throw new DecompressionException("Data seems to be corrupted");
                }

                int windowIndex = window.size() - (int)inStream.readNumber(lookbackBitSize);
                int sequenceLength = (int)inStream.readNumber(lengthBitSize);

                bytesToAdd = readFromWindow(window, windowIndex, sequenceLength);
            }

            for(int i = 0; i < bytesToAdd.length; i++) {
                window.add(bytesToAdd[i]);
            }
            output.addByteArray(bytesToAdd);

            if(window.size() > WINDOW_SIZE) {
                window = new LinkedList<>(window.subList(window.size() - WINDOW_SIZE, window.size()));
            }
        }

        output.reset();
        return output;
    }

    @Override
    public BitStream decompressByteString(byte[] sequence) throws DecompressionException {
        return decompressBitStream(new BitStream(sequence));
    }

    public String debugOutput(byte[] stream) {
        StringBuilder res = new StringBuilder();
        BitStream input = new BitStream(stream);

        while(input.hasBits()) {
            boolean newByte = !input.readBit();

            if(newByte) {
                res.append("0 ").append(input.readByte());
            } else {
                res.append("1 ").append(input.readNumber(lookbackBitSize)).append(" ").append(input.readNumber(lengthBitSize));
            }

            res.append(" | ");
        }
        res.delete(res.length() - 3, res.length());

        return res.toString();
    }

    private static int convertToBitCount(int length) {
        return (int)(Math.log(length) / Math.log(2)) + 1;
    }

    private int findInWindow(List<Byte> window, List<Byte> frame) {
        int result = -1;

        // FIND INDEX OF LAST OCCURRECNCE OF frame IN window
        for(int i = 0; i < window.size() - 1; i++) {
            int windowPointer = i;
            boolean flag = true;

            for(int j = 0; j < frame.size(); j++) {
                if(window.get(windowPointer) != frame.get(j)) {
                    flag = false;
                    break;
                }

                windowPointer++;
                if(windowPointer >= window.size()) {
                    windowPointer = i;
                }
            }

            if(flag) {
                result = i;
            }
        }

        return result;
    }

    private byte[] readFromWindow(List<Byte> window, int startPoint, int length) {
        byte[] result = new byte[length];
        int windowPointer = startPoint;

        for(int i = 0; i < length; i++) {
            result[i] = window.get(windowPointer);

            windowPointer++;
            if(windowPointer >= window.size()) {
                windowPointer = startPoint;
            }
        }

        return result;
    }

}
