package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

/**
 * Does nothing
 *
 * Created by Sergey on 2017-11-20
 */
public class FakeCompression implements ICompressor {

    @Override
    public BitStream compressBitStream(BitStream message) {
        return new BitStream(message.toByteArray());
    }

    @Override
    public BitStream decompressBitStream(BitStream sequence) throws DecompressionException {
        return new BitStream(sequence.toByteArray());
    }

    @Override
    public BitStream compressByteString(byte[] message) {
        return compressBitStream(new BitStream(message));
    }

    @Override
    public BitStream decompressByteString(byte[] sequence) throws DecompressionException {
        return decompressBitStream(new BitStream(sequence));
    }

}
