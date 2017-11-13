package com.example.denis.p7.algorithms.compression;

public class Node {
    Node left;
    Node right;
    byte value;
    Integer weight;


    public Node() {
        left = null;
        right = null;
    }

    public Node(byte value, Integer weight) {
        this.value = value;
        this.weight = weight;
        left = null;
        right = null;
    }

    public Node(Integer weight, Node left, Node right) {
        this.left = left;
        this.right = right;
        this.weight = weight;
    }

    public boolean isLeaf() {
        if (left == null && right == null) {
            return true;
        }
        return false;
    }

    public Node getRight() {
        return right;
    }

    public Node getLeft() {
        return left;
    }

    public Integer getWeight() {
        return weight;
    }

    public Byte getValue() {
        return value;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

}
