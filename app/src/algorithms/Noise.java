package algorithms;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains one static method {@code applyNoise} for making some noise for a bit (byte) sequence.
 */
public class Noise {
    /**
     * Flip some bits of the specified byte sequence with given probability.
     *
     * @param sequence Sequence of bits (bytes) to apply noise for
     * @param flipProb Probability of bit being flipped
     */
    public static void applyNoise(byte[] sequence, double flipProb) {
        if (sequence.length < 1) {
            return;
        }
        int i, j;
        for (i = 0; i < sequence.length; ++i) {
            for (j = 0; j < 8; ++j) {
                if (ThreadLocalRandom.current().nextDouble(0, 1) < flipProb) {
                    sequence[i] ^= 1 << j;
                }
            }
        }
    }
}
