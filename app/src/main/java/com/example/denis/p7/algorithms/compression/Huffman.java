package com.example.denis.p7.algorithms.compression;
import java.util.*;

public class Huffman {
    public Byte[] compressByteString(java.lang.Byte[] message) {
        Map<Byte, Integer> map = countFrequency(message);
        Node root = buildTree(map);
        Map<Byte, String> codes = new HashMap<Byte, String>();
        generateCode(root, codes, "");
        Byte[] encoded = encodeMessage(codes, message);
        return encoded;
    }

    private Map<Byte, Integer> countFrequency(Byte[] message) {
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

    private static Byte[] encodeMessage(Map<Byte, String> map, Byte[] message) {
        Byte[] encoded = new Byte[message.length];

        for (int i = 0; i < message.length; i++) {
            encoded[i] = Byte.parseByte(map.get(message[i]));
        }
        return encoded;
    }

}
