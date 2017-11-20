package com.example.denis.p7.algorithms.compression;

import java.util.Comparator;

public class MyComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if(node1.getWeight() == node2.getWeight()) {
            return node1.getValue() - node2.getValue();
        }

        return node1.getWeight() - node2.getWeight();
    }

}
