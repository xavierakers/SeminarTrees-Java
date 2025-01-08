package main.java.com.xakers.datastructures.bintree;

/**
 * Represents an empty node in a BinTree using the Flyweight design pattern.
 * This class is a singleton, ensuring only one instance exists
 *
 * @author Xavier Akers
 * @version 2025-01-07
 * @since 2025-01-07
 */
public class BTEmptyNode implements BTNode {

    private static final BTEmptyNode INSTANCE = new BTEmptyNode();

    /**
     * Private constructor to prevent instantiation
     * Use {@link #getInstance()} to access the singleton instance
     */
    private BTEmptyNode() {
        // Prevent instantiation
    }

    /**
     * Retrieves the singleton instance of BTEmptyNode
     *
     * @return the singleton instance of BTEmptyNode
     */
    public static BTEmptyNode getInstance() {
        return INSTANCE;
    }

    /**
     * Determines if this node is a leaf
     *
     * @return {@code true}, as an empty node is always a leaf
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * Prints a representation of the empty node
     */
    @Override
    public void print() {
        System.out.println("E");
    }
}
