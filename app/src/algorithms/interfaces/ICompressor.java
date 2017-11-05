package algorithms.interfaces;

import algorithms.exceptions.DecompressionException;

/**
 * Interface of Compressing/Decompressing algorithm.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-05
 */
public interface ICompressor {
    /**
     * Get an array of bytes that is compressed by the current compressing algorithm.
     *
     * @param message Array of bytes to compress using current compressing algorithm
     * @return Compressed sequence (array of bytes)
     */
    byte[] compressByteString(byte[] message);

    /**
     * Get an array of bytes that is decompressed version of a code previously compressed by the current algorithm.
     *
     * @param sequence Array of bytes to decompress which was previously compressed using current algorithm
     * @return Decompressed message (array of bytes)
     * @throws DecompressionException There is non-fixable mistake in a sequence of bytes
     */
    byte[] decompressByteString(byte[] sequence) throws DecompressionException;
}
