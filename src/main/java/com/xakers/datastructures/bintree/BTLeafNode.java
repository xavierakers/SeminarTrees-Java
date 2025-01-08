package main.java.com.xakers.datastructures.bintree;

/**
 * Represents a leaf node in a spatial binary tree.
 * THis node maintains a linked list of seminar objects,
 * where each seminar is stored as a {@link SeminarNode}.
 * <p>
 * This class supported adding, removing, and printing seminars,
 * as well as checking if the node is empty.
 *
 * @author Xavier Akers
 * @version 2025-01-08
 * @since 2025-01-05
 */
public class BTLeafNode implements BTNode {

    private SeminarNode head;

    /**
     * Constructs a new leaf node with an initial SeminarNode.
     *
     * @param seminarNode The SeminarNode to initialize the leaf node with.
     */
    public BTLeafNode(SeminarNode seminarNode) {
        this.head = seminarNode;
    }

    /**
     * Add seminar to the linked list as this leaf node.
     * The SeminarNodes are inserted in ascending order of their IDs.
     *
     * @param seminarNode The SeminarNode to add.
     */
    public void add(SeminarNode seminarNode) {
        SeminarNode curr = this.head;
        SeminarNode prev = null;

        // Traverse the linked list to find the appropriate position
        while (curr != null && seminarNode.getSeminar().id() > curr.getSeminar().id()) {
            prev = curr;
            curr = curr.getNext();
        }

        // Insert the node in the correct position
        if (prev == null) {
            seminarNode.setNext(head);
            head = seminarNode;
        } else {
            prev.setNext(seminarNode);
            seminarNode.setNext(curr);
        }
    }

    /**
     * Removes a SeminarNode with the specified key (ID) from the linked list.
     *
     * @param key The ID of the seminar to remove.
     * @return {@code true} if the seminar was found and removed, {@code false} otherwise.
     */
    public boolean remove(int key) {
        SeminarNode curr = this.head;
        SeminarNode prev = null;

        // Traverse the list to find the node with the matching key
        while (curr != null) {
            if (curr.getSeminar().id() == key) {
                if (prev == null) {
                    this.head = curr.getNext(); // Remove head node;
                } else {
                    prev.setNext(curr.getNext());
                }
                return true;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return false;
    }

    /**
     * Returns the head of the linked list in this leaf node.
     *
     * @return The head SeminarNode.
     */
    public SeminarNode getHead() {
        return this.head;
    }

    /**
     * Checks if the leaf node is empty (i.e., contains no seminars).
     *
     * @return {@code true} if the node is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Indicates that this is a leaf node.
     *
     * @return {@code true} because this node is always a leaf.
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * Prints the details of all seminars stored in this leaf node.
     * The output includes the number of seminars and their IDs.
     */
    @Override
    public void print() {
        SeminarNode curr = this.head;

        StringBuilder str = new StringBuilder();
        int count = 0;
        while (curr != null) {
            str.append(" ").append(curr.getSeminar().id());
            curr = curr.getNext();
            count++;
        }
        System.out.printf("Leaf with %d objects: %s\n", count, str);
    }
}
