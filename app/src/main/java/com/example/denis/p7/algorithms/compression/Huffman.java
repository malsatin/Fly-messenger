package com.example.denis.p7.algorithms.compression;

import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.interfaces.ICompressor;

import java.util.*;

public class Huffman implements ICompressor {

    public byte[] compressByteString(byte[] message) {
        Map<Byte, Integer> frequencyMap = countFrequency(message);
        Node root = buildTree(frequencyMap);

        Map<Byte, String> codes = new HashMap<>();
        if(root.isLeaf()) {
            codes.put(root.getValue(), "0");
        } else {
            generateCode(root, codes, "");
        }

        BitStream encoded = encodeMessage(codes, message);
        BitStream stream = serializeMessage(frequencyMap, encoded);

        return stream.toByteArray();
    }

    public byte[] decompressByteString(byte[] inputStream) throws DecompressionException {
        BitStream inStream = new BitStream(inputStream);

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

        return message.toByteArray();
    }

    private Map<Byte, Integer> countFrequency(byte[] message) {
        Map<Byte, Integer> map = new HashMap<>();
        for(byte oneByte : message) {
            if(map.containsKey(oneByte)) {
                map.put(oneByte, map.get(oneByte) + 1);
            } else {
                map.put(oneByte, 1);
            }
        }

        return map;
    }

    private Node buildTree(Map<Byte, Integer> map) {
        Queue<Node> queue = new PriorityQueue<>(map.size(), Comparator.comparingInt(Node::getWeight));
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

    private BitStream encodeMessage(Map<Byte, String> codesMap, byte[] message) {
        BitStream encoded = new BitStream();

        for(byte oneByte : message) {
            String code = codesMap.get(oneByte);

            for(int i = 0; i < code.length(); i++) {
                encoded.addBit(code.charAt(i) == '1');
            }
        }

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
