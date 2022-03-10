package com.physmo;

import com.physmo.buffers.TextBuffer;

public class Cursor {
    public int x = 0;
    public int y = 0;
    public int xMemory = 0;
    //TextBuffer textBuffer;
    CursorMetricSupplier cursorMetricSupplier;

    public Cursor(CursorMetricSupplier cursorMetricSupplier) {
        //this.textBuffer = textBuffer;
        this.cursorMetricSupplier=cursorMetricSupplier;
    }

    public void resetXMemory() {
        xMemory = x;
    }

    // return absolute character index for coords
    public int getDocumentIndex() {

        //int startOfLineIndex = textBuffer.getStartOfLineIndex(y);
        //return startOfLineIndex + x;

        // todo fix this
        return cursorMetricSupplier.getStartOfLineIndex(y)+x;
    }

    public void moveLeft() {
        x--;
        if (x < 0 && y > 0) {
            y--;
            jumpToEndOfLine();
        }
        xMemory = x;
    }

    public void jumpToEndOfLine() {
        int len = getCurrentLineLength();
        x = len;
        xMemory = x;
    }

    public int getCurrentLineLength() {
        //String line = textBuffer.getLine(y);
        //return line.length();
        return cursorMetricSupplier.getLineLength(y);
    }

    public void moveRight() {
        x++;
        if (x > getCurrentLineLength()) {
            x = 0;
            y++;
        }
        xMemory = x;
    }

    public void moveUp(int v) {
        y -= v;
        if (y < 0) y = 0;

        // Handle xMemory
        int currentLineLength = getCurrentLineLength();
        if (x > currentLineLength) {
            x = currentLineLength;
        } else if (xMemory < currentLineLength) {
            x = xMemory;
        }

    }

    public void moveDown(int v) {
        y += v;

        // Handle xMemory
        int currentLineLength = getCurrentLineLength();
        if (x > currentLineLength) {
            x = currentLineLength;
        } else if (xMemory < currentLineLength) {
            x = xMemory;
        }
    }

    public void jumpToStartOfLine() {
        x = 0;
        xMemory = x;
    }
}
