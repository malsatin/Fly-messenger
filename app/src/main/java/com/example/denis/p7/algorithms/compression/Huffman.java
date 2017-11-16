import java.util.*;

public class Huffman {
    private Node root;

    public byte[] compressByteString(byte[] message) {
        Map<Byte, Integer> map = countFrequency(message);
        this.root = buildTree(map);
        Map<Byte, String> codes = new HashMap<Byte, String>();
        if (root.isLeaf()) {
            codes.put(root.getValue(), "0");
        } else {
            generateCode(root, codes, "");
        }
        byte[] encoded = encodeMessage(codes, message);
        return encoded;
    }

    public byte[] decompressByteString(byte[] sequence) {
        byte[] message = new byte[sequence.length];
        BitStream stream = new BitStream(sequence);
        int i = 0;
        while (!stream.isEmpty()&&i<sequence.length) {
            Node current = root;
            while (current.left != null) {
                if (!stream.readBit()) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
            }
            message[i] = current.getValue();
            i++;
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
