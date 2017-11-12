package algorithms.tests;

import algorithms.helpers.BitStream;

/**
 * Fly-messenger
 * Created by Sergey on 2017-11-12
 */
public class BitStreamTest {

    public static void main(String[] args) {
        BitStream stream = new BitStream(new byte[]{10});

        System.out.println(stream.toString());
    }

}
