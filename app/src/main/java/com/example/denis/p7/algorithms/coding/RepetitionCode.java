package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class RepetitionCode implements ICoder {
    private static final int REPETITIONS = 3;

    @Override
    public BitStream encodeBitStream(BitStream message) {
        message.reset();
        BitStream answer = new BitStream();

        while (message.hasBits()) {
            boolean curBit = message.readBit();
            for (int k = 0; k < REPETITIONS; ++k) {
                answer.addBit(curBit);
            }
        }

        answer.reset();
        return answer;
    }

    @Override
    public BitStream decodeBitStream(BitStream sequence) throws DecodingException {
        sequence.reset();
        BitStream answer = new BitStream();

        while (sequence.hasBits()) {
            if (sequence.bitsRemain() < REPETITIONS) {
                throw new DecodingException("There remains only " + sequence.bitsRemain() + " bits, but needed at least " + REPETITIONS);
            }

            boolean[] bits = sequence.readBits(REPETITIONS);
            answer.addBit((bits[0] && bits[1]) || (bits[1] && bits[2]) || (bits[0] && bits[2]));
        }

        answer.reset();
        return answer;
    }
}
