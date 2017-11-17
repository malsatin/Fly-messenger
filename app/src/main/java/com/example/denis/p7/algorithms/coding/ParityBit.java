package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class ParityBit implements ICoder {
    private static final int BITS_OF_DATA = 8;
    private static final int BITS_ENCODED = BITS_OF_DATA + 1;

    @Override
    public byte[] encodeByteString(byte[] message) {
        BitStream in = new BitStream(message);
        BitStream out = new BitStream();
        long i, j;
        long cur, control;
        for (i = 0; i < in.size(); i += BITS_OF_DATA) {
            cur = in.readNumber(BITS_OF_DATA);
            out.addNumber(cur, BITS_OF_DATA);
            control = 0;
            for (j = 0; j < BITS_OF_DATA; j++) {
                control ^= (cur & (1L << j)) >>> j;
            }
            out.addBit((control & 1L) == 1);
        }
        return out.toByteArray();
    }

    @Override
    public byte[] decodeByteString(byte[] sequence) throws DecodingException {
        BitStream in = new BitStream(sequence);
        BitStream out = new BitStream();
        long i, j;
        long cur, control;
        for (i = 0; i < in.size() - BITS_OF_DATA; i += BITS_ENCODED) {
            cur = in.readNumber(BITS_ENCODED);
            control = 0;
            for (j = 0; j < BITS_ENCODED; j++) {
                control ^= (cur & (1L << j)) >>> j;
            }
            //System.out.println();
            if ((control & 1L) != 0) {
                throw new DecodingException();
            }
            out.addNumber(cur >>> 1, BITS_OF_DATA);
        }
        return out.toByteArray();
    }
}
