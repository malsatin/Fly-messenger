package com.example.denis.p7.algorithms.interfaces;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;

/**
 * Interface of Compressing/Decompressing algorithm.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-05
 */
public interface ICompressor {

    BitStream compressBitStream(BitStream message);

    BitStream decompressBitStream(BitStream sequence) throws DecompressionException;

    BitStream compressByteString(byte[] message);

    BitStream decompressByteString(byte[] sequence) throws DecompressionException;

}
