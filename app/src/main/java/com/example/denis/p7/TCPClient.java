package com.example.denis.p7;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import org.apache.commons.io.IOUtils;

class TCPClient {
    String hostname;
    int port;

    /**
     * Constructor for TCP client object.
     *
     * @param hostname
     *            Address of TCPServer
     * @param port
     *            Port of TCPServer
     */
    public TCPClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Request server to send matrix of bytes with message data
     *
     * @param alreadyHaveMessages
     *            Number of first messages that are not needed ~ index of latest
     *            message, use 0 to get all messages
     *
     * @return Messages in the following format: byte[x][y] where x - index of
     *         message, y - offset inside particular message, in case of valid
     *         absence of new messages returns byte[0][](can be checked with
     *         "getMessages().length == 0"), in case of connection error returns
     *         null.
     */
    public byte[][] getMessages(int alreadyHaveMessages) {
        try {
            // open new socket
            Socket s = new Socket(hostname, port);
            // write data to server
            s.getOutputStream().write(intToByteArray(alreadyHaveMessages));
            s.shutdownOutput();
            // receive reply from server
            InputStream is = s.getInputStream();
            byte[] dataSize = new byte[4];
            // see how many bytes has server sent as a reply

            is.read(dataSize);
            // read very first header 4 bytes indicating total message count
            int dataSizeInt = new BigInteger(dataSize).intValue();
            // create container for messages
            byte[][] result = new byte[dataSizeInt][];
            // fill container by sequentially reading data from server
            for (int i = 0; i < dataSizeInt; i++) {
                // read prefix: size of block
                byte[] messageSize = new byte[4];
                is.read(messageSize);
                // read message using size of prefix
                int length = new BigInteger(messageSize).intValue();
                byte[] message = new byte[length];
                IOUtils.read(is, message);
                // put message into output container
                result[i] = message;
            }

            // close connection
            s.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Send single data block to server
     *
     * @param data
     *            Reply from server - completeness state
     * @return Integer - error code: 0:(no error), 1:(internal database error,
     *         probably size limit exceeded which is ~800MB), 2:(received damaged
     *         message), 3:(connection with server failed)
     */
    public int sendMessage(byte[] data) {
        try {
            // open new socket
            Socket s = new Socket(hostname, port);
            // write data to server
            s.getOutputStream().write(data);
            s.shutdownOutput();
            // receive reply from server
            byte[] error = new byte[4];
            s.getInputStream().read(error);
            // close connection and return result
            s.close();
            return new BigInteger(error).intValue();
        } catch (Exception e) {
            return 3;
        }
    }

    /**
     * Convert integer into 4 bytes
     *
     * @param value
     *            Integer to convert
     * @return Byte array
     */
    private static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }
}