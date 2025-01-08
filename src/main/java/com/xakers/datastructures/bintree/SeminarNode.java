package main.java.com.xakers.datastructures.bintree;

import main.java.com.xakers.model.Seminar;

public class SeminarNode {
    private final Seminar seminar;
    private SeminarNode next;

    public SeminarNode(Seminar seminar) {
        this.seminar = seminar;
    }

    public Seminar getSeminar() {
        return seminar;
    }

    public SeminarNode getNext() {
        return next;
    }

    public void setNext(SeminarNode next) {
        this.next = next;
    }
}
