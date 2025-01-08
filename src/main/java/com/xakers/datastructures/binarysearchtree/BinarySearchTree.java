package main.java.com.xakers.datastructures.binarysearchtree;

import main.java.com.xakers.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic Binary Search Tree (BST) implementation that support various operations.
 * It allows inserting key-value pairs, searching by key, removing by key or key-value,
 * and performing rage and multi-search queries.
 *
 * @param <K> Type of keys in the tree (must be Comparable)
 * @param <V> Type of values associated with the keys
 * @author Xavier Akers
 * @version 2025-01-06
 * @since 2025-01-04
 */
public class BinarySearchTree<K extends Comparable<K>, V> {
    private class BSTNode {
        private Record<K, V> data;  // Data held in the node
        private BSTNode left;       // Left child node
        private BSTNode right;      // Right child node

        /**
         * Constructor to create a new BST node.
         *
         * @param key   The key for the new node
         * @param value The value for the new node
         */
        public BSTNode(K key, V value) {
            this.data = new Record<>(key, value);
            this.left = null;
            this.right = null;
        }

        /**
         * Returns the record associated with the node.
         *
         * @return The record of the node.
         */
        public Record<K, V> getData() {
            return data;
        }

        /**
         * Sets the record associated with the node.
         *
         * @param data The record for the node
         */
        public void setData(Record<K, V> data) {
            this.data = data;
        }

        /**
         * Returns the left child associated with the node.
         *
         * @return The left child of the node.
         */
        public BSTNode getLeft() {
            return left;
        }

        /**
         * Sets the left child associated with the node.
         *
         * @param left The left child of the node.
         */
        public void setLeft(BSTNode left) {
            this.left = left;
        }

        /**
         * Returns the right child associated with the node.
         *
         * @return The right child of the node.
         */
        public BSTNode getRight() {
            return right;
        }

        /**
         * Sets the right child associated with the node.
         *
         * @param right The right child of the node.
         */
        public void setRight(BSTNode right) {
            this.right = right;
        }
    }

    private BSTNode root;   // Root of the BST
    private int numNodes;   // Number of nodes in the BST

    /**
     * Constructs an empty BST
     */
    public BinarySearchTree() {
        this.root = null;
        this.numNodes = 0;
    }

    /**
     * Inserts a new key-value pair into the tree.
     * This method does not ensure uniqueness; duplicate keys are allowed.
     *
     * @param key   THe key to be inserted.
     * @param value The value associated with the key.
     */
    public void insert(K key, V value) {
        this.root = insert(root, key, value);
        this.numNodes++;
    }

    /**
     * Inserts a new key-value pair into the tree, ensuring uniqueness.
     * If the key already exists, it will not be inserted again.
     *
     * @param key   The key to be inserted.
     * @param value The value associated with the key.
     * @return True if the node was successfully inserted, false if the key already exists.
     */
    public boolean insertUnique(K key, V value) {
        boolean[] inserted = new boolean[1];
        this.root = insertUnique(root, key, value, inserted);
        if (inserted[0]) this.numNodes++;
        return inserted[0];
    }

    /**
     * Searches for a value associated with the given key.
     *
     * @param key The key to search for
     * @return The value associated with the key, or null if the key is not found.
     */
    public V search(K key) {
        return search(root, key);
    }

    /**
     * Searches for all values within the specified range of keys.
     *
     * @param low   The lower bound of the range (inclusive).
     * @param high  The upper bound of the range(inclusive).
     * @param count An array used to count the number of nodes visited during the search.
     * @return A list of values within the specified range.
     */
    public List<V> search(K low, K high, int[] count) {
        List<V> results = new ArrayList<>();
        count[0] = 0;
        rangeSearch(this.root, low, high, results, count);
        return results;
    }

    /**
     * Searches for all values associated with the given key, returning duplicates if any.
     *
     * @param key The key to search for.
     * @return A list of values associated with the key.
     */
    public List<V> multiSearch(K key) {
        List<V> results = new ArrayList<>();
        multiSearch(this.root, key, results);
        return results;
    }

    /**
     * Removes a node with the specified key from the tree.
     * The method returns a value associated with the removed key.
     *
     * @param key The key of the node to be removed.
     * @return The value of the removed ode, or null if the key is not found.
     */
    public V remove(K key) {
        List<V> deletedValue = new ArrayList<>(1);

        this.root = removeByKey(this.root, key, deletedValue);

        return deletedValue.isEmpty() ? null : deletedValue.getFirst();
    }

    /**
     * Removes a specific key-value pair from the tree.
     * Used for when there are duplicate keys in the tree.
     *
     * @param key   The key to be removed.
     * @param value The value to be removed.
     */
    public void remove(K key, V value) {
        this.root = removeByKeyValue(this.root, key, value);
    }

    /**
     * Prints the entire tree structure in a human-readable format.
     * It also displays the total number of records in the tree.
     */
    public void dump() {
        if (root == null) {
            System.out.println("This tree is empty");
            return;
        }

        dump(root, 0);
        System.out.printf("Number of records: %d\n", numNodes);
    }

    // ----------------------------------------------------------
    // Private Helper Methods
    // ----------------------------------------------------------

    /**
     * Helper method to insert a key-value pair into the tree.
     *
     * @param node  The current node in the tree.
     * @param key   The key to be inserted.
     * @param value The value to be inserted.
     * @return The updated tree node.
     */
    private BSTNode insert(BSTNode node, K key, V value) {
        // If node is null, create new node
        if (node == null) return new BSTNode(key, value);

        if (key.compareTo(node.data.getKey()) <= 0) {
            // Traverse left if less than or equal to (duplicates go left)
            node.setLeft(insert(node.left, key, value));
        } else {
            // Traverse right if greater than
            node.setRight(insert(node.right, key, value));
        }

        return node;
    }

    /**
     * Helper method to insert a key-value pair into the tree, ensuring uniqueness.
     *
     * @param node     The current node in the tree.
     * @param key      The key to be inserted.
     * @param value    The value to be inserted.
     * @param inserted A flag indicating if the node was inserted.
     * @return The updated tree node.
     */
    private BSTNode insertUnique(BSTNode node, K key, V value, boolean[] inserted) {
        // If node is null, create new node
        if (node == null) {
            inserted[0] = true;
            return new BSTNode(key, value);
        }

        if (key.compareTo(node.data.getKey()) < 0) {
            // Traverse left if less than
            node.setLeft(insertUnique(node.left, key, value, inserted));
        } else if (key.compareTo(node.data.getKey()) > 0) {
            // Traverse right if greater than
            node.setRight(insertUnique(node.right, key, value, inserted));
        } else {
            inserted[0] = false;
        }
        return node;

    }

    /**
     * Helper method to search for a key in the tree.
     *
     * @param node The current node.
     * @param key  The key to search for.
     * @return The value associated with the key, or null if the key is not found.
     */
    private V search(BSTNode node, K key) {
        if (node == null) return null;

        if (key.compareTo(node.getData().getKey()) < 0) {
            search(node.getLeft(), key);
        } else if (key.compareTo(node.getData().getKey()) > 0) {
            search(node.getRight(), key);
        }
        return node.getData().getValue();
    }

    /**
     * Helper method for performing a range search in the tree.
     *
     * @param node    The current node.
     * @param low     The lower bound of the range.
     * @param high    The upper bound of the range.
     * @param results A list to store the values within the range.
     * @param count   An array used to count the number of nodes visited during the search.
     */
    private void rangeSearch(BSTNode node, K low, K high, List<V> results, int[] count) {
        count[0]++;

        if (node == null) return;

        K key = node.getData().getKey();

        // Traverse left if left subtree may have keys >= low
        if (key.compareTo(low) >= 0) {
            rangeSearch(node.getLeft(), low, high, results, count);
        }

        // If current key is within range
        if (key.compareTo(low) >= 0 && key.compareTo(high) <= 0) {
            results.add(node.getData().getValue());
        }

        // Traverse right if right subtree may have keys <= high
        if (key.compareTo(high) <= 0) {
            rangeSearch(node.getRight(), low, high, results, count);
        }
    }

    /**
     * Helper method to perform a multi-search for a specific key.
     *
     * @param node    The current node.
     * @param key     The key to search for.
     * @param results A list to store the values associated with the keys.
     */
    private void multiSearch(BSTNode node, K key, List<V> results) {
        if (node == null) return;

        multiSearch(node.getLeft(), key, results);

        if (key.compareTo(node.getData().getKey()) == 0) {
            results.add(node.getData().getValue());
        }

        multiSearch(node.getRight(), key, results);

    }

    /**
     * Helper method to remove a node by key and return its associated value.
     *
     * @param node         The current node.
     * @param key          THe key to be removed.
     * @param deletedValue A list to store the value of the deleted node.
     * @return The updated tree node.
     */
    private BSTNode removeByKey(BSTNode node, K key, List<V> deletedValue) {
        if (node == null) return null;

        if (key.compareTo(node.getData().getKey()) < 0) {
            node.setLeft(removeByKey(node.getLeft(), key, deletedValue));
        } else if (key.compareTo(node.getData().getKey()) > 0) {
            node.setRight(removeByKey(node.getRight(), key, deletedValue));
        } else {
            deletedValue.add(node.getData().getValue());

            // Case 1: Node has no children
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            } else if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else {
                BSTNode temp = getMaxNode(node.getLeft());
                node.setData(temp.getData());
                node.setLeft(removeByKey(node.getLeft(), temp.getData().getKey(), deletedValue));
            }
        }

        return node;
    }

    /**
     * Helper method to remove a node by key-value pair.
     *
     * @param node  The current node.
     * @param key   The key to be removed.
     * @param value The value to be removed.
     * @return The updated node.
     */
    private BSTNode removeByKeyValue(BSTNode node, K key, V value) {
        if (node == null) return null;

        if (key.compareTo(node.getData().getKey()) < 0) {
            node.setLeft(removeByKeyValue(node.getLeft(), key, value));
        } else if (key.compareTo(node.getData().getKey()) > 0) {
            node.setRight(removeByKeyValue(node.getRight(), key, value));
        } else {
            if (node.getData().getValue().equals(value)) {
                if (node.getLeft() == null && node.getRight() == null) {
                    return null;
                } else if (node.getLeft() == null) {
                    return node.getRight();
                } else if (node.getRight() == null) {
                    return node.getLeft();
                } else {
                    BSTNode temp = getMaxNode(node.getLeft());
                    node.setData(temp.getData());
                    node.setLeft(removeByKeyValue(node.getLeft(), temp.getData().getKey(), temp.getData().getValue()));
                }
            }

        }
        return node;
    }

    /**
     * Helper method to print the tree.
     *
     * @param node  The current node.
     * @param level The current level in the tree.
     */
    private void dump(BSTNode node, int level) {
        if (node == null) {
            for (int i = 0; i < level; i++) {
                System.out.print("  ");
            }
            System.out.println("null");
            return;
        }

        dump(node.getRight(), level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.data.getKey());

        dump(node.getLeft(), level + 1);

    }

    /**
     * Helper method to find the maximum value node in a subtree.
     *
     * @param node The root node of the subtree.
     * @return THe maximum value node.
     */
    private BSTNode getMaxNode(BSTNode node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }
}
