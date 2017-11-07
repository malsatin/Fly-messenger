package algorithms.compression;

import algorithms.exceptions.DecompressionException;
import algorithms.interfaces.ICompressor;

/**
 * RLE compression algorithm
 *
 * Created by Sergey on 2017-11-07
 */
public class RLE implements ICompressor {

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
