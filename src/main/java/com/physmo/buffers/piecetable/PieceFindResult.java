package com.physmo.buffers.piecetable;

public class PieceFindResult {
    Node node;
    int offset; // search offset into piece.
    int piecePosition; // Char position in file of start of piece.

    boolean endOfBuffer=false;

    @Override
    public String toString() {
        return "PieceFindResult{" +
                "piece=" + node +
                ", offset=" + offset +
                ", piecePosition=" + piecePosition +
                '}';
    }
}
