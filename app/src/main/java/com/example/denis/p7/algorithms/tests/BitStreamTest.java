package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.helpers.BitStream;

/**
 * Fly-messenger
 * Created by Sergey on 2017-11-12
 */
public class BitStreamTest {

    public static void main(String[] args) {
        BitStream stream = new BitStream();
        stream.addBit(true);
        stream.addBit(true);
        stream.addBit(true);
        stream.addBit(false);
        stream.addBit(true);
        stream.addByte((byte)1);
        stream.addInt(2);

        System.out.println(stream.toString());
    }

}
