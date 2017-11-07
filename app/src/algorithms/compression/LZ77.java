package algorithms.compression;

import algorithms.exceptions.DecompressionException;
import algorithms.interfaces.ICompressor;

/**
 * LZ77 compression algorithm
 * <p>
 * Created by Sergey on 2017-11-07
 */
public class LZ77 implements ICompressor {
    @Override
    public byte[] compressByteString(byte[] message) {
        // TODO: implement algorithm
        return message.clone();
    }

    @Override
    public byte[] decompressByteString(byte[] sequence) throws DecompressionException {
        // TODO: implement algorithm
        return sequence.clone();
    }
}
