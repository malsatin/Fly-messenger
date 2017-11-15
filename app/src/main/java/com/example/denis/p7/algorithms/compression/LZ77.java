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
     * Optimal to be a multiple of 2
     */
    private final int WINDOW_SIZE = 16;
    /**
     * Shows, how many bytes can there be in a single sequence
     * Optimal to be a multiple of 2
     * FRAME_SIZE can be greater than WINDOW_SIZE
     */
    private final int FRAME_SIZE = 8;

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

        LinkedList<Byte> window = new LinkedList<>();
        ArrayList<Byte> frame;
        while(input.hasBits()) {
            int lastOccurrence = -1;
            int curOccurrence = 0;

            frame = new ArrayList<>(FRAME_SIZE);
            while(input.hasBits() && frame.size() < FRAME_SIZE) {
                frame.add(input.readByte());

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
                    input.revert(BitStream.BYTE_SIZE);
                }

                output.addBit(1);
                output.addNumber(lastOccurrence, lookbackBitSize);
                output.addNumber(frame.size(), lengthBitSize);
            }

            window.addAll(frame);

            if(window.size() > WINDOW_SIZE) {
                window = new LinkedList<>(window.subList(window.size() - WINDOW_SIZE, window.size()));
            }
        }

        System.out.println(output.toString());
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

    private int findInWindow(List window, List frame) {
        // todo: FIND INDEX OF LAST OCCURRECNCE OF frame IN window
        return -1;
    }

}
