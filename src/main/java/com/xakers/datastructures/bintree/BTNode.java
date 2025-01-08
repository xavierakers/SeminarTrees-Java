package main.java.com.xakers.datastructures.bintree;

/**
 * Represents a generic node in a binary tree structure.
 * This interface define the basic behavior for both internal nodes
 * and leaf nodes in a spatial binary tree.
 * <p>
 * Implementing classes should specify whether then ode is a leaf
 * and provide a method to print or format the node's details.
 *
 * @author Xavier Akers
 * @version 2025-01-06
 * @since 2025-01-06
 */
public interface BTNode {

    /**
     * Determined if the current node is a leaf node.
     *
     * @return {@code true} if the node is a leaf, {@code false} otherwise.
     */
    boolean isLeaf();

    /**
     * Prints or formats the current node's details.
     * Implementing classes should define the specific of how
     * the node's information is presented.
     */
    void print();
}
