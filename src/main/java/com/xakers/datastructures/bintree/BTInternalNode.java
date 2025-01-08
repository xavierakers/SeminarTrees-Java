package main.java.com.xakers.datastructures.bintree;

/**
 * Represents an internal node in composite BinTree
 * This class implements the {@code BTNode} interface and provides
 * methods to manage left and right child nodes
 *
 * @author Xavier Akers
 * @version 2025-01-07
 * @since 2025-01-07
 */
public class BTInternalNode implements BTNode {

    private BTNode left;
    private BTNode right;

    /**
     * Constructs an InternalNode with specified left and right child nodes
     *
     * @param left  Left child
     * @param right Right child
     */
    public BTInternalNode(BTNode left, BTNode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Retrieves the left child node
     *
     * @return the left child node
     */
    public BTNode getLeft() {
        return left;
    }

    /**
     * Updates the left child node
     *
     * @param left the new left child node to set
     */
    public void setLeft(BTNode left) {
        this.left = left;
    }

    /**
     * Retrieves the right child node
     *
     * @return the right child node
     */
    public BTNode getRight() {
        return right;
    }

    /**
     * Updates the right child node
     *
     * @param right the new right child node to set
     */
    public void setRight(BTNode right) {
        this.right = right;
    }

    /**
     * Checks if the node is a leaf
     *
     * @return {@code false}, as this node is always an internal node
     */
    @Override
    public boolean isLeaf() {
        return false;
    }

    /**
     * Prints a representation of the internal node
     */
    @Override
    public void print() {
        System.out.println("I");
    }
}
