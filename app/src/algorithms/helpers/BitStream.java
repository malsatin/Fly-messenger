package algorithms.helpers;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * BitStream makes work with bits simpler.
 * That is useful, when you are working with encoding/decoding/compressing/decompressing.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-12
 */
public class BitStream {
    private final int BYTE_SIZE = 8;
    private final int INT_SIZE = 32;
    private final int LONG_SIZE = 64;

    /**
     * Number of the current bit in the stream.
     */
    private int pointer = 0;

    /**
     * Size of the stream in bits.
     */
    private int size = 0;

    /**
     * Storage of bits. Not optimal one, but flexible and relatively fast.
     */
    private ArrayList<Long> storage;

    private ListIterator<Long> iter;

    /**
     * Creates empty stream, that you can fill up.
     */
    public BitStream() {
        storage = new ArrayList<>();
        iter = storage.listIterator();
    }

    /**
     * Creates stream, filled with your data.
     *
     * @param stream Data to be added immediately
     */
    public BitStream(byte[] stream) {
        this();
        addByteArray(stream);
    }

    /**
     * @param bitsCount
     * @return
     */
    public long read(int bitsCount) {
        if (bitsCount < 1) {
            throw new InvalidParameterException("Can't read non-positive number of bits");
        }
        if (bitsCount > 64) {
            throw new InvalidParameterException("Can't return more than 64 bits");
        }

        // TODO

        return 0;
    }

    /**
     * Returns an array of booleans of provided length as a sequence of next unread bits in a stream.
     *
     * @param bitsCount Number of bits to read
     * @return Array of boolean means of {@code bitsCount} next succeed bits in a stream
     */
    public boolean[] readAsBool(int bitsCount) {
        if (bitsCount < 1) {
            return new boolean[0];
        }
        int resCount = Math.min(bitsCount, size - pointer);
        boolean[] res = new boolean[resCount];
        long cur = 0L;
        for (int i = 0; i < resCount; ++i) {
            byte pos = (byte) (i % LONG_SIZE);
            if (pos == 0) {
                cur = iter.next();
            }
            res[i] = (cur & (1 << pos)) != 0;
        }
        return res;
    }

    /**
     * @return
     */
    public int readInt() {
        return (int) read(INT_SIZE);
    }

    /**
     * @return
     */
    public char readChar() {
        return (char) readInt();
    }

    /**
     * @param data
     */
    public void addByte(byte data) {
        // TODO
    }

    public void addByteArray(byte[] data) {
        for (byte aData : data) {
            addByte(aData);
        }
    }

    /**
     * @param data
     */
    public void addInt(int data) {
        // TODO
    }

    /**
     * @param data
     */
    public void addChar(char data) {
        addInt((int) data);
    }

    /**
     * Appends 1 bit to the end of the stream
     *
     * @param data true == 1; false == 0
     */
    public void addBit(boolean data) {
        // TODO
    }

    /**
     * Appends 1 bit to the end of the stream
     *
     * @param data Least significant bit will be used
     */
    public void addBit(int data) {
        data &= 1; // Remains only LSB

        addBit(data == 1);
    }

    /**
     * Skips number of bits in the stream that you will specify
     * If amount of remaining bits is less than count to skip, then pointer is simply moved to the end
     *
     * @param bitsToSkip
     */
    public void skip(int bitsToSkip) {
        pointer += bitsToSkip;
        if (pointer > size) {
            pointer = size;
        }
    }

    /**
     * @return Size of the hole stream
     */
    public int size() {
        return size;
    }

    /**
     * @return How many bit remain to the end of the stream
     */
    public int bitsRemain() {
        return size - pointer;
    }

    /**
     * @return Is the steam doesn't contain any more bits
     */
    public boolean isEmpty() {
        return bitsRemain() == 0;
    }

    /**
     * Resets the pointer, so the stream can be read again
     *
     * @return Previous pointer number
     */
    public int reset() {
        int tmp = pointer;

        pointer = 0;

        return tmp;
    }

    /**
     * Completely clears stream and resets everything
     */
    public void clear() {
        pointer = 0;
        size = 0;

        storage = new ArrayList<>();
    }

    /**
     * @return Array of bytes (from stream start to the end)
     */
    public byte[] toByteArray() {
        // TODO

        return null;
    }

    @Override
    public String toString() {
        // TODO

        return super.toString();
    }

}
