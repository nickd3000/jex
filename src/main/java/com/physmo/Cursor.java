package com.physmo;

import com.physmo.buffers.TextBuffer;

public class Cursor {
    public int x=0;
    public int y=0;
    TextBuffer textBuffer;
    public Cursor(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }

    public int getDocumentIndex() {
        //textBuffer.getLine(y);
        int startOfLineIndex = textBuffer.getStartOfLineIndex(y);
        return startOfLineIndex+x;
    }

    public void moveLeft() {
        x--;
        if (x<0 && y>0) {
            y--;
            jumpToEndOfLine();
        }
    }
    public void moveRight() {
        x++;
        if (x>getCurrentLineLength()){
            x=0;
            y++;
        }
    }
    public void moveUp(int v) {
        y-=v;
        if (y<0) y=0;
    }
    public void moveDown(int v) {
        y+=v;
    }

    public void jumpToEndOfLine() {
        int len = getCurrentLineLength();
        x=len;
    }
    public void jumpToStartOfLine() {
        x=0;
    }

    public int getCurrentLineLength() {
        String line = textBuffer.getLine(y);
        return line.length();
    }
}
