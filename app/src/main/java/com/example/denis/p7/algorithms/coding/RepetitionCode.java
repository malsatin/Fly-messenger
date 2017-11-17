package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class RepetitionCode implements ICoder {

    private static final int REPETITIONS = 3;

    @Override
    public byte[] encodeByteString(byte[] message) {
        BitStream stream = new BitStream(message);
        BitStream answer = new BitStream();

        while(stream.hasBits()) {
            boolean curBit = stream.readBit();
            for (int k = 0; k < REPETITIONS; k++) {
                answer.addBit(curBit);
            }
        }

        return answer.toByteArray();
    }

    @Override
    public byte[] decodeByteString(byte[] sequence) throws DecodingException {
        BitStream stream = new BitStream(sequence);
        BitStream answer = new BitStream();

        while(stream.hasBits()) {
            if(stream.bitsRemain() < REPETITIONS) {
                throw new DecodingException("There remains only " + stream.bitsRemain() + " bits, but needed at least " + REPETITIONS);
            }

            boolean[] bits = stream.readBits(REPETITIONS);
            answer.addBit((bits[0] && bits[1]) || (bits[1] && bits[2]) || (bits[0] && bits[2]));
        }

        return answer.toByteArray();
    }
}
