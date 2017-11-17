package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

public class HammingCode implements ICoder {
    private static final int BITS_OF_DATA = 4;
    private static final int PARITY_BITS = 3;

    @Override
    public byte[] encodeByteString(byte[] message) {
        BitStream in = new BitStream(message);
        BitStream out = new BitStream();
        BitStream parityBits = new BitStream();
        long[] data = new long[BITS_OF_DATA];
        long i;
        int j;
        for (i = 1; i < in.size(); i += BITS_OF_DATA) {
            for (j = 0; j < BITS_OF_DATA; ++j) {
                data[j] = in.readBit() ? 1L : 0L;
                out.addBit(data[j] == 1);
            }
            /* Attention! Constant block of code. To be refactored if any of constants above constant will change! */
            parityBits.addBit((data[0] ^ data[1] ^ data[3]) == 1);
            parityBits.addBit((data[0] ^ data[2] ^ data[3]) == 1);
            parityBits.addBit((data[1] ^ data[2] ^ data[3]) == 1);
            /* End of constant block of code */
        }
        out.addByteArray(parityBits.toByteArray());
        return out.toByteArray();
    }

    @Override
    public byte[] decodeByteString(byte[] sequence) throws DecodingException {
        BitStream in = new BitStream(sequence);
        BitStream out = new BitStream();
        long i;
        for (i = 0; i < in.size() * BITS_OF_DATA / (BITS_OF_DATA + PARITY_BITS) - BITS_OF_DATA - PARITY_BITS; i += BitStream.BYTE_SIZE) {
            out.addByte(in.readByte());  // Wrong! Check on the way...
        }
        //in.size() - (BitStream.BYTE_SIZE / BITS_OF_DATA) * in.size() % BITS_OF_DATA; i += BITS_OF_DATA
        return out.toByteArray();
    }
}
