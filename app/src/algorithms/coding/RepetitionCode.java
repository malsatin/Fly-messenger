package algorithms.coding;

import algorithms.exceptions.DecodingException;
import algorithms.interfaces.ICoder;

import java.util.LinkedList;

public class RepetitionCode implements ICoder {
    @Override
    public byte[] encodeByteString(byte[] message) {
        LinkedList<Byte> list = new LinkedList<>();
        for (byte b : message) {  // TODO replace with algorithm
            list.add(b);
        }
        return getArray(list);
    }

    @Override
    public byte[] decodeByteString(byte[] sequence) throws DecodingException {
        LinkedList<Byte> list = new LinkedList<>();
        for (byte b : sequence) {  // TODO replace with algorithm
            list.add(b);
        }
        return getArray(list);
    }
}
