package main.java.com.xakers.datastructures.bintree;

import main.java.com.xakers.model.Seminar;

/**
 * Represents a node in a linked list structure, where each node contains
 * a reference to a {@link Seminar} object and a pointer to the next node
 * in the list.
 * <p>
 * This class is designed to facilitate the storage and traversal of seminar
 * objects in a linked structure, allowing dynamic addition and management.
 *
 * @author Xavier Akers
 * @version 2025-01-08
 * @since 2025-01-06
 */
public class SeminarNode {

    private final Seminar seminar; // The seminar object associated with this node.
    private SeminarNode next; // Pointer to the next SeminarNode in the linked list.

    /**
     * Constructs a new SeminarNode with the given Seminar object.
     *
     * @param seminar The seminar associated with this node. Cannot be null.
     * @throws IllegalArgumentException if {@code seminar} is null.
     */
    public SeminarNode(Seminar seminar) {
        if (seminar == null) {
            throw new IllegalArgumentException("error: Seminar cannot be null.");
        }
        this.seminar = seminar;
    }

    /**
     * Returns the Seminar objects associated with this node.
     *
     * @return The seminar associated with this node.
     */
    public Seminar getSeminar() {
        return seminar;
    }

    /**
     * Returns the next SeminarNode in the linked list.
     *
     * @return The next node, or {@code null} if this is the last node in the list.
     */
    public SeminarNode getNext() {
        return next;
    }

    /**
     * Sets the next SeminarNode in the linked list.
     *
     * @param next The node to be sat as the next node. Can be {@code null}
     *             to indicate the end of the list.
     */
    public void setNext(SeminarNode next) {
        this.next = next;
    }
}
