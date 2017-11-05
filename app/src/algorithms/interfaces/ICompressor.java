package algorithms.interfaces;

import algorithms.exceptions.DecompressionException;

/**
 * Fly-messenger
 * Created by Sergey on 2017-11-05
 */
public interface ICompressor {

    /**
     * Get an array of bytes that is encoded by the current coding algorithm.
     *
     * @param message Array of bytes to encode using current coding algorithm
     * @return Encoded sequence (array of bytes)
     */
    byte[] compressByteString(byte[] message);

    /**
     * Get an array of bytes that is decoded version of a code previously encoded by the current algorithm.
     *
     * @param sequence Array of bytes to decode which was previously encoded using current algorithm
     * @return Decoded message (array of bytes)
     */
    byte[] decompressByteString(byte[] sequence) throws DecompressionException;

}
