package main.java.com.xakers.datastructures.binarysearchtree;

import main.java.com.xakers.model.Record;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<K extends Comparable<K>, V> {
    private class BSTNode {
        private Record<K, V> data;
        private BSTNode left;
        private BSTNode right;

        public BSTNode(K key, V value) {
            this.data = new Record<>(key, value);
            this.left = null;
            this.right = null;
        }

        public Record<K, V> getData() {
            return data;
        }

        public void setData(Record<K, V> data) {
            this.data = data;
        }

        public BSTNode getLeft() {
            return left;
        }

        public void setLeft(BSTNode left) {
            this.left = left;
        }

        public BSTNode getRight() {
            return right;
        }

        public void setRight(BSTNode right) {
            this.right = right;
        }
    }

    private BSTNode root;
    private int numNodes;

    public BinarySearchTree() {
        this.root = null;
        this.numNodes = 0;
    }

    public void insert(K key, V value) {
        this.root = insert(root, key, value);
        this.numNodes++;
    }

    public boolean insertUnique(K key, V value) {
        boolean[] inserted = new boolean[1];
        this.root = insertUnique(root, key, value, inserted);
        if (inserted[0]) this.numNodes++;
        return inserted[0];
    }

    // Exact Search
    public V search(K key) {
        return search(root, key);
    }

    // Range Search
    public List<V> search(K low, K high, int[] count) {
        List<V> results = new ArrayList<>();
        count[0] = 0;
        rangeSearch(this.root, low, high, results, count);
        return results;
    }

    // Multi Search
    public List<V> multiSearch(K key) {
        List<V> results = new ArrayList<>();
        multiSearch(this.root, key, results);
        return results;
    }

    public V remove(K key) {
        List<V> deletedValue = new ArrayList<>(1);

        this.root = removeByKey(this.root, key, deletedValue);

        return deletedValue.isEmpty() ? null : deletedValue.get(0);
    }

    public void remove(K key, V value) {
        return;
    }

    public void dump() {
        if (root == null) {
            System.out.println("This tree is empty");
            return;
        }

        dump(root, 0);
        System.out.printf("Number of records: %d\n", numNodes);
    }

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

    private V search(BSTNode node, K key) {
        if (node == null) return null;

        if (key.compareTo(node.getData().getKey()) < 0) {
            search(node.getLeft(), key);
        } else if (key.compareTo(node.getData().getKey()) > 0) {
            search(node.getRight(), key);
        }
        return node.getData().getValue();
    }

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

    private void multiSearch(BSTNode node, K key, List<V> results) {
        if (node == null) return;

        multiSearch(node.getLeft(), key, results);

        if (key.compareTo(node.getData().getKey()) == 0) {
            results.add(node.getData().getValue());
        }

        multiSearch(node.getRight(), key, results);

    }

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

    private BSTNode getMaxNode(BSTNode node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }

        return node;
    }
}
