package main.java.com.xakers.datastructures.bintree;

import main.java.com.xakers.model.Seminar;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a spatial binary tree for organizing two-dimensional data (assumes Seminar object)
 * based on coordinates (x, y). The tree alternates splitting by x and y
 * coordinates at each level to partition the space effectively.
 * <p>
 * This implementation supports operations such as insertion, deletion,
 * and searching within the defined spatial boundaries.
 *
 * @author Xavier Akers
 * @version 2025-01-08
 * @since 2025-01-06
 */
public class BinTree {

    private BTNode root;    // Root node of the binary tree
    // Shared singleton instance representing an empty node in the tree
    private static final BTNode EMPTY_NODE = BTEmptyNode.getInstance();
    private final int xMax; // Maximum x-coordinate boundary
    private final int yMax; // Maximum y-coordinate boundary

    /**
     * Constructs an empty spatial binary tree with defined boundaries.
     * The tree initially has no nodes other than the {@code EMPTY_NODE}.
     *
     * @param xMax The maximum x-coordinate boundary for the spatial region.
     * @param yMax The maximum y-coordinate boundary for the spatial region.
     */
    public BinTree(int xMax, int yMax) {
        if (xMax <= 0 || yMax <= 0) {
            throw new IllegalArgumentException("error: worldSize must be greater than 0.");
        }
        this.root = EMPTY_NODE;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    /**
     * Inserts a seminar into the tree
     *
     * @param seminar the seminar to insert
     */
    public void insertSeminar(Seminar seminar) {
        SeminarNode seminarNode = new SeminarNode(seminar);
        root = insert(this.root, seminarNode,
                this.xMax / 2, this.yMax / 2,
                this.xMax / 2, this.yMax / 2, 0);
    }

    /**
     * Searches for seminars within a given radius from a point (x, y)
     *
     * @param x      X-coordinate of query point
     * @param y      Y-coordinate of query point
     * @param radius Search radius
     * @param count  Array to track number of nodes visited
     * @return List of seminars within the search radius
     */
    public List<Seminar> search(int x, int y, int radius, int[] count) {
        List<Seminar> results = new ArrayList<>();
        count[0] = 0;
        search(this.root, x, y, radius,
                xMax / 2, yMax / 2, 0,
                results, count);
        return results;
    }

    /**
     * Removes a key from the tree.
     *
     * @param key The key to be removed.
     * @param x   X-coordinate of the key to be removed.
     * @param y   Y-coordinate of the key to be removed.
     */
    public void remove(int key, int x, int y) {
        this.root = remove(this.root, key, x, y, xMax / 2, yMax / 2, xMax / 2, yMax / 2, 0);
    }


    /**
     * Dumps the entire binary tree structure to the console, starting from the root.
     * Each level of the tree is indented to visually represent the hierarchy.
     */
    public void dump() {
        dump(root, 0);
    }

    // ----------------------------------------------------------
    // Private Helper Methods
    // ----------------------------------------------------------

    /**
     * Helper method to handle insertion logic for both leaf and internal nodes
     *
     * @param node        the current node
     * @param seminarNode the seminar node to insert
     * @param xDiscrim    the x-coordinate discriminator
     * @param yDiscrim    the y-coordinate discriminator
     * @param width       the width of the region
     * @param height      the height of the region
     * @param level       the current tree level
     * @return the updated tree node
     */
    private BTNode insert(BTNode node, SeminarNode seminarNode, int xDiscrim, int yDiscrim,
                          int width, int height, int level) {

        // If node is empty, make new LeafNode
        if (node == EMPTY_NODE) {
            return new BTLeafNode(seminarNode);
        }

        if (node.isLeaf())
            // If node is internalNode -> insert accordingly
            return handleLeafNodeInsertion((BTLeafNode) node, seminarNode, xDiscrim, yDiscrim, width, height, level);
        else
            // If node is leafNode -> insert accordingly
            return handleInternalNodeInsertion((BTInternalNode) node, seminarNode, xDiscrim, yDiscrim, width, height, level);


    }


    /**
     * Helper method to handle insertion when the current node is a leaf
     * If the new seminar has the same coordinates as the existing seminar in the leaf,
     * it is added to the list of seminars at that coordinate
     * Otherwise, the leaf is converted into an internal node, and both the existing and new seminar are re-insterted
     *
     * @param leafNode    the current leaf node
     * @param seminarNode the seminar node to insert
     * @param xDiscrim    the x-coordinate discriminator
     * @param yDiscrim    the y-coordinate discriminator
     * @param width       the width of the region
     * @param height      the height of the region
     * @param level       the current tree level
     * @return the updated tree node
     */
    private BTNode handleLeafNodeInsertion(BTLeafNode leafNode, SeminarNode seminarNode,
                                           int xDiscrim, int yDiscrim,
                                           int width, int height, int level) {

        // Check if new seminar has same coordinates as current leafNode -> we can add to the LList at that coordinate
        if (leafNode.getHead().getSeminar().x() == seminarNode.getSeminar().x()
                && leafNode.getHead().getSeminar().y() == seminarNode.getSeminar().y()) {
            leafNode.add(seminarNode);
            return leafNode;
        }

        // Convert leaf to internal node and reinsert both seminars
        BTInternalNode internalNode = new BTInternalNode(EMPTY_NODE, EMPTY_NODE);

        handleInternalNodeInsertion(internalNode, leafNode.getHead(), xDiscrim, yDiscrim, width, height, level);
        handleInternalNodeInsertion(internalNode, seminarNode, xDiscrim, yDiscrim, width, height, level);

        return internalNode;
    }

    /**
     * Helper method to handle insertion when the current node is an internal node.
     * Determines whether to continue in the left or right subtree based on the coordinate discriminator.
     *
     * @param internalNode the current internal node
     * @param seminarNode  the seminar node to insert
     * @param xDiscrim     the x-coordinate discriminator
     * @param yDiscrim     the y-coordinate discriminator
     * @param width        the width of the region
     * @param height       the height of the region
     * @param level        the current tree level
     * @return the updated tree node
     */
    private BTNode handleInternalNodeInsertion(BTInternalNode internalNode, SeminarNode seminarNode,
                                               int xDiscrim, int yDiscrim,
                                               int width, int height, int level) {
        if (level % 2 == 0) { // Vertical split
            if (seminarNode.getSeminar().x() < xDiscrim) { // Left subtree might overlap
                internalNode.setLeft(insert(internalNode.getLeft(), seminarNode,
                        xDiscrim - (width / 2), yDiscrim, width / 2, height, level + 1));
            } else { // Right subtree might overlap
                internalNode.setRight(insert(internalNode.getRight(), seminarNode,
                        xDiscrim + (width / 2), yDiscrim, width / 2, height, level + 1));
            }
        } else { // Horizontal split
            if (seminarNode.getSeminar().y() < yDiscrim) { // Bottom subtree might overlap
                internalNode.setLeft(insert(internalNode.getLeft(), seminarNode,
                        xDiscrim, yDiscrim - (height / 2), width, height / 2, level + 1));
            } else { // Top subtree might overlap
                internalNode.setRight(insert(internalNode.getRight(), seminarNode,
                        xDiscrim, yDiscrim + (height / 2), width, height / 2, level + 1));
            }
        }

        return internalNode;

    }

    /**
     * Helper method to recursively search the tree for seminars within a given radius
     *
     * @param node     Current node in the tree
     * @param x        X-coordinate of query point
     * @param y        Y-coordinate of query point
     * @param radius   Search radius
     * @param xDiscrim X-coordinate of the current discriminator
     * @param yDiscrim Y-coordinate of the current discriminator
     * @param level    Current depth in the tree
     * @param results  List to store seminars found within the radius
     * @param count    Array to track the number of nodes visited
     */
    private void search(BTNode node, int x, int y, int radius,
                        int xDiscrim, int yDiscrim, int level,
                        List<Seminar> results, int[] count) {
        count[0]++; // Increment visited node count

        // Stop if node is empty
        if (node == EMPTY_NODE) return;

        if (node.isLeaf())
            // Process leaf nodes directly
            searchLeafNode((BTLeafNode) node, x, y, radius, results);
        else
            // Process internal nodes recursively
            searchInternalNode((BTInternalNode) node, x, y, radius,
                    xDiscrim, yDiscrim, level,
                    results, count);

    }

    /**
     * Helper method to search a leaf node for seminars within a given radius
     *
     * @param leafNode Leaf node to search
     * @param x        X-coordinate of query point
     * @param y        Y-coordinate of query point
     * @param radius   Search radius
     * @param results  List to store seminars found within the radius
     */
    private void searchLeafNode(BTLeafNode leafNode, int x, int y, int radius, List<Seminar> results) {
        int radiusSq = radius * radius;
        SeminarNode curr = leafNode.getHead();

        // Traverse all seminars in the leaf node
        while (curr != null) {
            double distanceSq = distanceSq(x, y, curr.getSeminar().x(), curr.getSeminar().y());
            // Add seminar to reults if it lies within the radius
            if (distanceSq <= radiusSq) {
                results.add(curr.getSeminar());
            }
            curr = curr.getNext();
        }
    }

    /**
     * Helper method to recursively search an internal node's children based on the query point
     *
     * @param internalNode Internal Node to search
     * @param x            X-coordinate of query point
     * @param y            Y-coordinate of query point
     * @param radius       Search radius
     * @param xDiscrim     X-coordinate of current discriminator
     * @param yDiscrim     Y-coordinate of current discriminator
     * @param level        Current depth in the tree
     * @param results      List to store seminars found within the radius
     * @param count        Array to track the number of nodes visited
     */
    private void searchInternalNode(BTInternalNode internalNode, int x, int y, int radius,
                                    int xDiscrim, int yDiscrim, int level,
                                    List<Seminar> results, int[] count) {
        if (level % 2 == 0) { // X-axis
            if (x - radius <= xDiscrim) // Search left if it overlaps query radius
                search(internalNode.getLeft(), x, y, radius, xDiscrim - (xMax / (1 << (level + 1))), yDiscrim, level, results, count);
            if (x + radius > xDiscrim)// Search right if it overlaps query radius
                search(internalNode.getRight(), x, y, radius, xDiscrim + (xMax / (1 << (level + 1))), yDiscrim, level, results, count);
        } else { // Y-axis
            if (y - radius <= yDiscrim) // Search left if it overlaps query radius
                search(internalNode.getLeft(), x, y, radius, xDiscrim, yDiscrim - (yMax / (1 << (level + 1))), level, results, count);
            if (y + radius > yDiscrim) // Search right if it overlaps query radius
                search(internalNode.getRight(), x, y, radius, xDiscrim, yDiscrim + (yMax / (1 << (level + 1))), level, results, count);

        }
    }

    /**
     * Helper method to remove a node with the specified key from the binary tree.
     *
     * @param node     The current node.
     * @param key      The key to be removed.
     * @param x        X-coordinate of the key's location.
     * @param y        Y-coordinate of the key's location.
     * @param xDiscrim X-coordinate of current discriminator.
     * @param yDiscrim Y-coordinate of current discriminator.
     * @param width    The width of the region.
     * @param height   The height of the region.
     * @param level    The current tree level.
     * @return The updated tree node.
     */
    private BTNode remove(BTNode node, int key, int x, int y,
                          int xDiscrim, int yDiscrim,
                          int width, int height, int level) {
        if (node == EMPTY_NODE) return node;

        if (node.isLeaf())
            return removeLeafNode((BTLeafNode) node, key);
        else
            return removeInternalNode((BTInternalNode) node, key, x, y, xDiscrim, yDiscrim, width, height, level);

    }

    /**
     * Helper method to remove a key from a leaf node. If the leaf becomes empty after removal,
     * it is replaced with the EMPTY_NODE.
     *
     * @param leafNode The leaf node from which to remove the key.
     * @param key      The key to remove.
     * @return The updated tree node.
     */
    private BTNode removeLeafNode(BTLeafNode leafNode, int key) {
        if (leafNode.remove(key) && !leafNode.isEmpty()) return leafNode;
        return EMPTY_NODE;
    }


    /**
     * Helper method to remove a key from an internal node and adjusts the tree structure as needed.
     * Decides whether ot traverse left or right based on the current level and coordinates.
     *
     * @param internalNode The internal node from which to remove the key.
     * @param key          The key to remove.
     * @param x            X-coordinate of the key's location.
     * @param y            Y-coordinate of the key's location.
     * @param xDiscrim     X-coordinate of current discriminator.
     * @param yDiscrim     Y-coordinate of current discriminator.
     * @param width        The width of the region.
     * @param height       The height of the region.
     * @param level        The current tree level.
     * @return The updated tree node.
     */
    private BTNode removeInternalNode(BTInternalNode internalNode, int key, int x, int y,
                                      int xDiscrim, int yDiscrim,
                                      int width, int height, int level) {
        if (level % 2 == 0) {
            if (x < xDiscrim) {
                internalNode.setLeft(remove(internalNode.getLeft(), key, x, y,
                        xDiscrim - (width / 2), yDiscrim, width / 2, height, level + 1));
            } else {
                internalNode.setRight(remove(internalNode.getRight(), key, x, y,
                        xDiscrim + (width / 2), yDiscrim, width / 2, height, level + 1));
            }
        } else {
            if (y < yDiscrim) {
                internalNode.setLeft(remove(internalNode.getLeft(), key, x, y,
                        xDiscrim, yDiscrim - (height / 2), width, height / 2, level + 1));
            } else {
                internalNode.setRight(remove(internalNode.getRight(), key, x, y,
                        xDiscrim, yDiscrim + (height / 2), width, height / 2, level + 1));
            }
        }

        if (internalNode.getLeft() == EMPTY_NODE && internalNode.getRight() == EMPTY_NODE) {
            return EMPTY_NODE;
        }
        return internalNode;
    }

    /**
     * Recursively dumps the structure of a binary tree node and its children to the console.
     * Indents the output based on the level of the node in the tree for readability.
     *
     * @param node  the current node to dump
     * @param level the depth level of the current node in the tree (used for indentation)
     */
    private void dump(BTNode node, int level) {
        // Print indentation for current level
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        // Print the current node's information
        node.print();
        // If the current node is an internal node, recursively dump left and right children
        if (!node.isLeaf()) {
            dump(((BTInternalNode) node).getLeft(), level + 1);
            dump(((BTInternalNode) node).getRight(), level + 1);
        }
    }

    /**
     * Calculates the squared Euclidean distance between two points
     *
     * @param x1 X-coordinate of the first point
     * @param y1 Y-coordinate of the first point
     * @param x2 X-coordinate of the second point
     * @param y2 Y-coordinate of the second point
     * @return The squared Euclidean distance between two points.
     */
    private double distanceSq(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }
}
