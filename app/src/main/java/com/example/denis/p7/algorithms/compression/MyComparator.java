package com.example.denis.p7.algorithms.compression;

import java.util.Comparator;

public class MyComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        return node1.getWeight() - node2.getWeight();
    }
}
