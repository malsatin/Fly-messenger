package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class HammingCode implements ICoder {
    private static final int BITS_OF_DATA = 4;
    private static final int PARITY_BITS = 3;

    @Override
    public BitStream encodeBitStream(BitStream message) {
        if (message.size() % BITS_OF_DATA != 0) {
            throw new IllegalArgumentException("wrong message length");
        }
        message.reset();
        BitStream out = new BitStream();
        BitStream parityBits = new BitStream();
        long[] data = new long[BITS_OF_DATA];
        long i;
        int j;
        for (i = 1; i < message.size(); i += BITS_OF_DATA) {
            for (j = 0; j < BITS_OF_DATA; ++j) {
                data[j] = message.readBit() ? 1L : 0L;
                out.addBit(data[j] == 1);
            }
            /* Attention! Constant block of code. To be refactored if any of constants above constant will change! */
            parityBits.addBit((data[0] ^ data[1] ^ data[2]) == 1);
            parityBits.addBit((data[1] ^ data[2] ^ data[3]) == 1);
            parityBits.addBit((data[0] ^ data[1] ^ data[3]) == 1);
            /* End of constant block of code */
        }
        out.addByteArray(parityBits.toByteArray());

        out.reset();
        return out;
    }

    @Override
    public BitStream decodeBitStream(BitStream sequence) throws DecodingException {
        sequence.reset();
        BitStream out = new BitStream();
        long i;
        long dataLength = sequence.size() * BITS_OF_DATA / (BITS_OF_DATA + PARITY_BITS) - BITS_OF_DATA - PARITY_BITS;
        for (i = 0; i < dataLength; i += BitStream.BYTE_SIZE) {
            out.addByte(sequence.readByte());
        }
        long[] data = new long[BITS_OF_DATA];
        byte syndrome;
        int j;
        for (i = 0; i < out.size(); i += BITS_OF_DATA) {
            for (j = 0; j < BITS_OF_DATA; ++j) {
                data[j] = out.readBit() ? 1L : 0L;
            }
            /* Attention! Constant block of code. To be refactored if any of constants above constant will change! */
            syndrome = (byte) (4 * (sequence.readNumber(1) ^ data[0] ^ data[1] ^ data[2]));
            syndrome += (byte) (2 * (sequence.readNumber(1) ^ data[1] ^ data[2] ^ data[3]));
            syndrome += (byte) (sequence.readNumber(1) ^ data[0] ^ data[1] ^ data[3]);
            switch (syndrome) {
                case 0:
                case 1:
                case 2:
                case 4:
                    break;
                case 3:
                    data[3] = data[3] == 1 ? 0 : 1;
                    break;
                case 5:
                    data[0] = data[0] == 1 ? 0 : 1;
                    break;
                case 6:
                    data[2] = data[2] == 1 ? 0 : 1;
                    break;
                case 7:
                    data[1] = data[1] == 1 ? 0 : 1;
                    break;
                default:
                    throw new DecodingException("Error detected while decoding! Please, ask for the data again.");
            }
            /* End of constant block of code */
        }

        out.reset();
        return out;
    }
}