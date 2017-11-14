package com.example.denis.p7.algorithms.helpers;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * BitStream makes work with bits simpler.
 * That is useful, when you are working with encoding/decoding/compressing/decompressing.
 * <p>
 * All bits goes in this ( --> ) direction.
 * <p>
 * Created by Sergey Malyutkin on 2017-11-12
 */
public class BitStream {

    /**
     * Sizes int bits of some primitive types
     */
    private final int BYTE_SIZE = 8;
    private final int INT_SIZE = 32;
    private final int LONG_SIZE = 64;

    /**
     * How many bits to display in a block in toString method.
     */
    private final int DISPLAY_BLOCK_SIZE = 8;

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

    /**
     * Creates empty stream, that you can fill up.
     */
    public BitStream() {
        storage = new ArrayList<>();
    }

    /**
     * Creates stream, filled with your data.
     *
     * @param stream Data to be added immediately into the stream
     */
    public BitStream(byte[] stream) {
        this();

        addByteArray(stream);
    }

    /**
     * Creates stream, filled with your data and cuts it to the size.
     * Useful, if you have not full last byte and don't want unwanted data.
     *
     * @param stream Data to be added immediately into the stream
     * @param size   Size int bits of the stream
     */
    public BitStream(byte[] stream, int size) {
        this(stream);

        this.size = size;
    }

    /**
     * @return One bit from the stream
     */
    public boolean readBit() {
        if(isEmpty()) {
            throw new InvalidParameterException("There is no more data in stream");
        }

        long curLong = storage.get(pointer / LONG_SIZE);
        int indexInLong = pointer % LONG_SIZE;

        boolean result = getBit(curLong, indexInLong);

        pointer++;

        return result;
    }

    /**
     * Returns an array of booleans of provided length as a sequence of next unread bits in a stream.
     * If there remains less bits than bitsCount, then only remained bits will be returned.
     *
     * @param bitsCount Number of bits to read
     * @return Array of boolean means of {@code bitsCount} next succeed bits in a stream
     */
    public boolean[] readAsBool(int bitsCount) {
        if(bitsCount < 1) {
            throw new InvalidParameterException("Can't read non-positive number of bits");
        }
        int resCount = Math.min(bitsCount, bitsRemain());
        if(resCount == 0) {
            throw new InvalidParameterException("There is no more data in stream");
        }
        boolean[] result = new boolean[resCount];

        for(int i = 0; i < resCount; i++) {
            long curLong = storage.get(pointer / LONG_SIZE);
            int indexInLong = pointer % LONG_SIZE;

            result[i] = getBit(curLong, indexInLong);
            pointer++;
        }

        return result;
    }

    /**
     * If there remains less bits than bitsCount,
     * then missing bits will be filled up with zeros.
     *
     * @param bitsCount Number of bits to read
     * @return Array of bits in boolean form
     */
    public boolean[] readUnsafe(int bitsCount) {
        boolean[] bits = new boolean[bitsCount];
        boolean[] safeBits = readAsBool(bitsCount);

        for(int i = 0, l = safeBits.length; i < l; i++) {
            bits[i] = safeBits[i];
        }
        for(int i = safeBits.length; i < bitsCount; i++) {
            bits[i] = false;
        }

        return bits;
    }

    private long readNumber(int bitsCount) {
        if(bitsCount > 64) {
            throw new InvalidParameterException("Can't return more than 64 bits");
        }

        long result = 0;
        boolean[] bits = readAsBool(bitsCount);

        for(boolean bit : bits) {
            result = (result << 1) & (bit ? 1 : 0);
        }

        return result;
    }

    /**
     * @return Next int from the stream
     */
    public int readInt() {
        return (int)readNumber(INT_SIZE);
    }

    /**
     * @return Next char from the stream
     */
    public char readChar() {
        return (char)readInt();
    }

    /**
     * @param data Bit to append at the end of the stream
     */
    public void addBit(boolean data) {
        int indexInLong = size % LONG_SIZE;

        long newLong;
        if(indexInLong != 0) {
            int curLongInd = size / LONG_SIZE;
            newLong = setBit(storage.get(curLongInd), indexInLong, data);

            storage.set(curLongInd, newLong);
        } else {
            newLong = (data ? 1 : 0) << (LONG_SIZE - 1);

            storage.add(newLong);
        }

        size++;
    }

    /**
     * @param data Bit to append at the end of the stream (LSB of int is considered)
     */
    public void addBit(int data) {
        data &= 1; // Remains only LSB

        addBit(data == 1);
    }

    /**
     * @param data Byte to append at the end of the stream
     */
    public void addByte(byte data) {
        for(int i = BYTE_SIZE - 1; i >= 0; i--) {
            addBit(((data >> i) & 1) == 1);
        }
    }

    /**
     * @param data Byte sequence to append at the end of the stream
     */
    public void addByteArray(byte[] data) {
        for(byte aData : data) {
            addByte(aData);
        }
    }

    /**
     * @param data Integer to append at the end of the stream
     */
    public void addInt(int data) {
        for(int i = INT_SIZE - 1; i >= 0; i--) {
            addBit(((data >> i) & 1) == 1);
        }
    }

    /**
     * @param data Character to append at the end of the stream
     */
    public void addChar(char data) {
        addInt((int)data);
    }

    /**
     * Skips number of bits in the stream that you will specify.
     * If amount of remaining bits is less than count to skip, then pointer is simply moved to the end.
     *
     * @param bitsToSkip Number of bits to skip in the stream
     */
    public void skip(int bitsToSkip) {
        pointer += bitsToSkip;

        if(pointer > size) {
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
     * Resets the pointer, so the stream can be read again.
     *
     * @return Previous pointer number
     */
    public int reset() {
        int tmp = pointer;

        pointer = 0;

        return tmp;
    }

    /**
     * Completely clears stream and resets everything.
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
        int bytesCount = size / BYTE_SIZE;
        if(size % BYTE_SIZE != 0) {
            bytesCount += 1;
        }

        int tmpPointer = 0;
        byte[] bytes = new byte[bytesCount];
        for(int i = 0; i < storage.size(); i++) {
            for(int j = 0; j < LONG_SIZE; j++) {
                int curByteInd = tmpPointer / BYTE_SIZE;
                byte curBit = (byte)(storage.get(i) >> j & 1);
                bytes[curByteInd] = (byte)((bytes[curByteInd] << 1) + curBit);

                tmpPointer++;
                if(tmpPointer >= size) {
                    break;
                }
            }
        }

        return bytes;
    }

    /**
     * @return Array of bits in boolean form (from stream start to the end)
     */
    private boolean[] toBitArray() {
        boolean[] bits = new boolean[size];

        int tmpPointer = 0;
        for(int i = 0; i < storage.size(); i++) {
            for(int j = 0; j < LONG_SIZE; j++) {
                bits[tmpPointer] = getBit(storage.get(i), j);

                tmpPointer++;
                if(tmpPointer >= size) {
                    break;
                }
            }
        }

        return bits;
    }

    /**
     * @return Binary representation, divided into blocks
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean[] bits = toBitArray();
        //System.out.println(Arrays.toString(bits));

        for(int i = 0; i < size; i++) {
            result.append(bits[i] ? 1 : 0);
            if(i % DISPLAY_BLOCK_SIZE == 0 && i != 0) {
                result.append(" ");
            }
        }

        int missedBitsCount = DISPLAY_BLOCK_SIZE - (size % DISPLAY_BLOCK_SIZE);
        if(missedBitsCount != DISPLAY_BLOCK_SIZE) {
            for(int i = 0; i < missedBitsCount; i++) {
                result.append("_");
            }
        }

        return result.toString();
    }

    /**
     * Gets certain bit in long integer
     *
     * @param row       Long integer to find in
     * @param leftIndex Index of the bit from the left
     * @return Bit value
     */
    private boolean getBit(long row, int leftIndex) {
        int offset = LONG_SIZE - leftIndex - 1;
        long shifted = (row >> (offset)) & 1L;

        return shifted == 1;
    }

    /**
     * Sets certain bit in long integer
     *
     * @param row       Long integer to modify
     * @param leftIndex Index of the bit from the left
     * @param value     What value to set
     * @return New long integer
     */
    private long setBit(long row, int leftIndex, boolean value) {
        if(value) {
            row |= 1L << (LONG_SIZE - leftIndex - 1);
        } else {
            row &= ~(1L << (LONG_SIZE - leftIndex - 1));
        }

        return row;
    }

}
