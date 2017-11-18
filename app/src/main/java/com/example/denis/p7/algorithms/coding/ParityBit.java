package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class ParityBit implements ICoder {
    private static final int BITS_OF_DATA = 8;
    private static final int BIT_PORTIONS = BITS_OF_DATA + 1;

    @Override
    public BitStream encodeBitStream(BitStream message) {
        if (message.size() % BITS_OF_DATA != 0) {
            throw new IllegalArgumentException("wrong message length");
        }
        message.reset();
        BitStream out = new BitStream();
        long i, j, cur, control;
        for (i = 0; i < message.size(); i += BITS_OF_DATA) {
            cur = message.readNumber(BITS_OF_DATA);
            out.addNumber(cur, BITS_OF_DATA);
            control = 0;
            for (j = 0; j < BITS_OF_DATA; j++) {
                control ^= (cur & (1L << j)) >>> j;
            }
            out.addBit((control & 1L) == 1);
        }
        return out;
    }

    @Override
    public BitStream decodeBitStream(BitStream sequence) throws DecodingException {
        sequence.reset();
        BitStream out = new BitStream();
        long i, j, cur, control;
        for (i = 0; i < sequence.size() - BITS_OF_DATA; i += BIT_PORTIONS) {
            cur = sequence.readNumber(BIT_PORTIONS);
            control = 0;
            for (j = 0; j < BIT_PORTIONS; j++) {
                control ^= (cur & (1L << j)) >>> j;
            }
            if ((control & 1L) != 0) {
                throw new DecodingException("Error detected while decoding! Please, ask for the data again.");
            }
            out.addNumber(cur >>> 1, BITS_OF_DATA);
        }
        return out;
    }
}