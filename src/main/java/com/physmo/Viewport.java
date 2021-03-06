package com.physmo;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.buffers.TextBuffer;

// TODO: rename to text viewport?
public class Viewport extends Panel {

    TextBuffer textBuffer;
    int scrollOffset = 0;
    Cursor cursor;

    public Viewport() {
    }

    public Cursor getCurser() {
        return cursor;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        cursor = new Cursor(textBuffer);
    }

    @Override
    void draw(TextGraphics tg) {
        if (textBuffer == null) return;

        ColorRepo.setNormalTextColor(tg);
        Point panelPos = getCombinedPosition();
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');

        int usableWidth = width;

        int lineCount = textBuffer.getLineCount();
        String currentLine = "";
        for (int i = 0; i < height; i++) {
            if (i < lineCount) {
                currentLine = textBuffer.getLine(i);
            } else {
                currentLine = ".";
            }

            if (currentLine.length()>usableWidth)
                currentLine = currentLine.substring(0,usableWidth);

            tg.putString(panelX, panelY + i, currentLine);
        }

    }
}
