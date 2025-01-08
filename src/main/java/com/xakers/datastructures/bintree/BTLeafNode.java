package main.java.com.xakers.datastructures.bintree;

import main.java.com.xakers.model.Seminar;

public class BTLeafNode implements BTNode {

    private SeminarNode head;


    public BTLeafNode(SeminarNode seminarNode) {
        this.head = seminarNode;
    }

    /**
     * Add seminar to the LLink at this node (coord)
     *
     * @param seminarNode Seminar node to add
     */
    public void add(SeminarNode seminarNode) {
        // Insert appropriately into LList
        SeminarNode curr = this.head;
        SeminarNode prev = null;

        while (curr != null && seminarNode.getSeminar().id() > curr.getSeminar().id()) {
            prev = curr;
            curr = curr.getNext();
        }

        if (prev == null) {
            seminarNode.setNext(head);
            head = seminarNode;
        } else {
            prev.setNext(seminarNode);
            seminarNode.setNext(curr);
        }
    }

    public boolean remove(int key) {
        SeminarNode curr = this.head;
        SeminarNode prev = null;

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

    public SeminarNode getHead() {
        return this.head;
    }

    public boolean isEmpty() {
        return this.head == null;
    }


    @Override
    public boolean isLeaf() {
        return true;
    }

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
