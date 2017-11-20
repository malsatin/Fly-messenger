package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

import java.util.*;

public class Huffman implements ICompressor {

    @Override
    public BitStream compressBitStream(BitStream inStream) {
        Map<Byte, Integer> frequencyMap = countFrequency(inStream);
        Node root = buildTree(frequencyMap);

        Map<Byte, String> codes = new HashMap<>();
        if(root.isLeaf()) {
            codes.put(root.getValue(), "0");
        } else {
            generateCode(root, codes, "");
        }

        BitStream encoded = encodeMessage(codes, inStream);
        BitStream stream = serializeMessage(frequencyMap, encoded);

        stream.fillGap();
        stream.reset();
        return stream;
    }

    @Override
    public BitStream compressByteString(byte[] message) {
        return compressBitStream(new BitStream(message));
    }

    @Override
    public BitStream decompressBitStream(BitStream inStream) throws DecompressionException {
        Map<Byte, Integer> frequencyMap = deserializeMap(inStream);
        Node root = buildTree(frequencyMap);

        int messageSize = 0; // Message size is sum of all frequencies
        for(Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            messageSize += entry.getValue();
        }

        BitStream message = new BitStream();
        for(int i = 0; i < messageSize; i++) {
            Node current = root;
            while(!current.isLeaf()) {
                if(!inStream.hasBits()) {
                    throw new DecompressionException("Not in the leaf, but stream already ended");
                }

                current = inStream.readBit() ? current.getRight() : current.getLeft();
            }

            message.addByte(current.getValue());
        }

        message.reset();
        return message;
    }

    @Override
    public BitStream decompressByteString(byte[] inputStream) throws DecompressionException {
        return decompressBitStream(new BitStream(inputStream));
    }

    private Map<Byte, Integer> countFrequency(BitStream message) {
        Map<Byte, Integer> map = new HashMap<>();

        while(message.hasBits()) {
            byte oneByte = message.readByte();

            if(map.containsKey(oneByte)) {
                map.put(oneByte, map.get(oneByte) + 1);
            } else {
                map.put(oneByte, 1);
            }
        }

        message.reset();
        return map;
    }

    private Node buildTree(Map<Byte, Integer> map) {
        Queue<Node> queue = new PriorityQueue<>(map.size(), new MyComparator());
        for(Map.Entry<Byte, Integer> entry : map.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while(queue.size() > 1) {
            Node node1 = queue.remove();
            Node node2 = queue.remove();
            Node newNode = new Node(node1.getWeight() + node2.getWeight(), node1, node2);

            queue.add(newNode);
        }

        return queue.remove();
    }

    private void generateCode(Node node, Map<Byte, String> codesMap, String codeString) {
        if(node.isLeaf()) {
            codesMap.put(node.getValue(), codeString);
        } else {
            generateCode(node.left, codesMap, codeString + '0');
            generateCode(node.right, codesMap, codeString + '1');
        }
    }

    private BitStream encodeMessage(Map<Byte, String> codesMap, BitStream message) {
        BitStream encoded = new BitStream();

        while(message.hasBits()) {
            byte oneByte = message.readByte();
            String code = codesMap.get(oneByte);

            for(int i = 0; i < code.length(); i++) {
                encoded.addBit(code.charAt(i) == '1');
            }
        }

        message.reset();
        return encoded;
    }

    private BitStream serializeMessage(Map<Byte, Integer> frequencyMap, BitStream message) {
        BitStream stream = new BitStream();

        // Find maximum length of number to represent frequency
        int maxLength = 0;
        for(Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            int tmpLength = countBitLength(entry.getValue());

            maxLength = Math.max(maxLength, tmpLength);
        }

        stream.addInt(frequencyMap.size());
        stream.addInt(maxLength);
        for(Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            stream.addByte(entry.getKey());
            stream.addNumber((long)entry.getValue(), maxLength);
        }

        // We don't need to store message or code length, because Huffman codes are prefix-free
        while(message.hasBits()) {
            stream.addBit(message.readBit());
        }

        return stream;
    }

    private Map<Byte, Integer> deserializeMap(BitStream stream) {
        Map<Byte, Integer> map = new HashMap<>();

        int entriesCount = stream.readInt();
        int frequencySize = stream.readInt();
        for(int i = 0; i < entriesCount; i++) {
            map.put(stream.readByte(), (int)stream.readNumber(frequencySize));
        }

        return map;
    }

    private static int countBitLength(int number) {
        return (int)(Math.log(number) / Math.log(2)) + 1;
    }

}
