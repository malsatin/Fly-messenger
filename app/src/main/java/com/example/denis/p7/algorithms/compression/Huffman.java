package com.example.denis.p7.algorithms.compression;
import java.util.*;

public class Huffman {
    public byte[] compressByteString(byte[] message) {
        Map<Byte, Integer> map = countFrequency(message);
        Node root = buildTree(map);
        Map<Byte, String> codes = new HashMap<Byte, String>();
        if (root.isLeaf()) {
            codes.put(root.getValue(), "0");
        } else {
            generateCode(root, codes, "");
        }
        byte[] encoded = encodeMessage(codes, message);
        return encoded;
    }

    public Byte[] decompressByteString(byte[] sequence, Node root) {
        Byte[] message = new Byte[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            Node current = root;
            while ((current.getLeft() != null)) {
                if (sequence[i] != 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
                i++;
            }
            message[i] = current.getValue();
        }
        return message;
    }

    private Map<Byte, Integer> countFrequency(byte[] message) {
        Map<Byte, Integer> map = new HashMap<>();
        for (int i = 0; i < message.length; i++) {
            if (map.containsKey(message[i])) {
                map.put(message[i], map.get(message[i]) + 1);
            } else {
                map.put(message[i], 1);
            }
        }
        return map;
    }

    private Node buildTree(Map<Byte, Integer> map) {
        Queue<Node> queue = new PriorityQueue<Node>(map.size(), new MyComparator());
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            Node node1 = queue.remove();
            Node node2 = queue.remove();
            Node node = new Node(node1.getWeight() + node2.getWeight(), node1, node2);
            queue.add(node);
        }

        return queue.remove();
    }

    private static void generateCode(Node node, Map<Byte, String> map, String s) {
        if (node.isLeaf()) {
            map.put(node.getValue(), s);
            return;
        }
        generateCode(node.left, map, s + '0');
        generateCode(node.right, map, s + '1');
    }

    private static byte[] encodeMessage(Map<Byte, String> map, byte[] message) {

        BitStream encoded = new BitStream();

        for (int i = 0; i < message.length; i++) {
            String code = map.get(message[i]);
            for (int j = 0; j < code.length(); j++) {
                encoded.addBit(code.charAt(j) == '1' ? 1 : 0);
            }
        }
        return encoded.toByteArray();
    }

}
