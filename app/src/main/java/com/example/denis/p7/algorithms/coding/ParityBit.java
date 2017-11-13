package com.example.denis.p7.algorithms.coding;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.interfaces.ICoder;

import java.util.LinkedList;

public class ParityBit implements ICoder {
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
