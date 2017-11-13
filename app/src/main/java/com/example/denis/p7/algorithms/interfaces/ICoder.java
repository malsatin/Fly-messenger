package com.example.denis.p7.algorithms.interfaces;

import com.example.denis.p7.algorithms.exceptions.DecodingException;

import java.util.LinkedList;

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
    byte[] encodeByteString(byte[] message);

    /**
     * Get an array of bytes that is decoded version of a code previously encoded by the current algorithm.
     *
     * @param sequence Array of bytes to decode which was previously encoded using current algorithm
     * @return Decoded message (array of bytes)
     * @throws DecodingException There is non-fixable mistake in a sequence of bytes
     */
    byte[] decodeByteString(byte[] sequence) throws DecodingException;

    /**
     * Get an array of bytes out of LinkedList of bytes.
     *
     * @param list LinkedList of bytes
     * @return Array of bytes
     */
    default byte[] getArray(LinkedList<Byte> list) {
        if (list.isEmpty()) {
            return new byte[0];
        }
        byte[] res = new byte[list.size()];
        int i = 0;
        for (byte b : list) {
            res[i] = b;
            ++i;
        }
        return res;
    }
}
