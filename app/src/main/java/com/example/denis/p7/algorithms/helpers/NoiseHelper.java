package com.example.denis.p7.algorithms.helpers;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains one static method {@code applyNoise} for making some noise for a bit sequence.
 * <p>
 * Created by Denis Chernikov on 2017-11-05
 */
public class NoiseHelper {
    /**
     * Flip some bits of the specified byte sequence with given probability.
     *
     * @param sequence Sequence of bits to apply noise for
     * @param flipProb Probability of bit being flipped
     */
    public static void applyNoise(BitStream sequence, double flipProb) {
        if (sequence.size() < 1) {
            return;
        }
        byte[] toChange = sequence.toByteArray();
        int i, j;
        for (i = 0; i < sequence.size(); ++i) {
            for (j = 0; j < 8; ++j) {
                if (ThreadLocalRandom.current().nextDouble(0, 1) < flipProb) {
                    toChange[i] ^= 1 << j;
                }
            }
        }
        sequence.clear();
        sequence.addByteArray(toChange);
    }
}
