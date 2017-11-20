package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICoder;

/**
 * Fly-messenger
 * Created by Sergey on 2017-11-20
 */
public class FakeCoding implements ICoder {

    @Override
    public BitStream encodeBitStream(BitStream message) {
        return new BitStream(message.toByteArray());
    }

    @Override
    public BitStream decodeBitStream(BitStream sequence) throws DecodingException {
        return new BitStream(sequence.toByteArray());
    }

}
