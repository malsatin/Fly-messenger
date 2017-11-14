package com.example.denis.p7.algorithms.tests;

import com.example.denis.p7.algorithms.helpers.BitStream;

import java.util.Arrays;

/**
 * Fly-messenger
 * Created by Sergey on 2017-11-12
 */
public class BitStreamTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();

        // BitStream(byte[] stream, int size) remains untested
        // readUnsafe(int bitsCount) remains untested
    }

    public static void test1() {
        BitStream stream = new BitStream(new byte[] {(byte)1, (byte)63});
        stream.addByte((byte)127);
        stream.addBit(false);
        stream.addBit(true);

        System.out.println("Result:   " + stream.toString());
        System.out.println("Expected: 00000001 00111111 01111111 01______");
        System.out.println();
    }

    public static void test2() {
        BitStream s = new BitStream();
        s.addBit(1);
        s.addBit(0);
        s.addByte((byte)12);
        s.addInt(500);
        s.addChar('a');

        System.out.println("Result:   " + s.readBit() + " " + s.readBit() + " " + s.readByte() + " " + s.readInt() + " " + s.readChar());
        System.out.println("Expected: true false 12 500 a");
        System.out.println();
    }

    public static void test3() {
        BitStream s = new BitStream();
        s.addInt(123);

        int i1 = s.readInt();
        s.reset();
        int i2 = s.readInt();

        System.out.println("Result:   " + i1 + " " + i2);
        System.out.println("Expected: 123 123");
        System.out.println();
    }

    public static void test4() {
        BitStream s = new BitStream();
        s.addInt(123);
        s.addInt(123);

        s.clear();

        System.out.println("Result:   " + s.toString());
        System.out.println("Expected: ");
        System.out.println();
    }

    public static void test5() {
        BitStream s = new BitStream();
        s.addBit(1);
        s.addBit(1);
        s.addBit(0);
        s.addBit(1);

        s.skip(2);

        System.out.println("Result:   " + s.readBit() + " " + s.readBit());
        System.out.println("Expected: false true");
        System.out.println();
    }

    public static void test6() {
        BitStream s = new BitStream();
        s.addInt(423);
        s.addInt('b');

        s.skip(51);

        System.out.println("Result:   " + s.size() + " " + s.bitsRemain());
        System.out.println("Expected: 64 13");
        System.out.println();
    }

    public static void test7() {
        BitStream s = new BitStream();
        s.addInt(423);
        s.addInt('b');

        System.out.println("Result:   " + Arrays.toString(s.toByteArray()));
        System.out.println("Expected: [0, 0, 1, -89, 0, 0, 0, 98]");
        System.out.println();
    }

}
