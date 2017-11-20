package com.example.denis.p7.algorithms.interfaces;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.helpers.BitStream;

/**
 * Interface of Encoding/Decoding algorithm.
 * <p>
 * Created by Denis Chernikov on 2017-11-02
 */
public interface ICoder {
    /**
     * Get an array of bytes that is encoded by the current coding algorithm.
     *
     * @param message Array of bytes to encode using current coding algorithm
     * @return Encoded sequence (array of bytes)
     */
    default BitStream encodeByteString(byte[] message) {
        return encodeBitStream(new BitStream(message));
    }

    /**
     * Get an array of bytes that is decoded version of a code previously encoded by the current algorithm.
     *
     * @param sequence Array of bytes to decode which was previously encoded using current algorithm
     * @return Decoded message (array of bytes)
     * @throws DecodingException There is non-fixable mistake in a sequence of bytes
     */
    default BitStream decodeByteString(byte[] sequence) throws DecodingException {
        return decodeBitStream(new BitStream(sequence));
    }

    /**
     * Get new BitStream that is encoded by the current coding algorithm.
     * <p>
     * NOTE: BitStream becomes done after the execution
     *
     * @param message Sequence of bits to encode using current coding algorithm
     * @return Encoded sequence
     */
    BitStream encodeBitStream(BitStream message);

    /**
     * Get new BitStream that is decoded version of a code previously encoded by the current algorithm.
     * <p>
     * NOTE: BitStream becomes done after the execution
     *
     * @param sequence Sequence of bits to decode which was previously encoded using current algorithm
     * @return Decoded message
     * @throws DecodingException There is non-fixable mistake in a sequence of bits
     */
    BitStream decodeBitStream(BitStream sequence) throws DecodingException;
}
