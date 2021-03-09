package com.physmo.buffers.piecetable;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public int start;
    public int length;
    int bufferId;
    List<Integer> lineStarts = new ArrayList<>(); // Offset from start of the section.

    public Node(int start, int length, int bufferId) {
        this.start = start;
        this.length = length;
        this.bufferId = bufferId;
    }

    public Node() {
    }

    @Override
    public String toString() {
        return "Node{" +
                "start=" + start +
                ", length=" + length +
                ", bufferId=" + bufferId +
                '}';
    }
}
