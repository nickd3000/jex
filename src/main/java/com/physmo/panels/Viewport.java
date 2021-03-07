package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.TextBuffer;

// TODO: rename to text viewport?
public class Viewport extends Panel {

    int id;
    TextBuffer textBuffer;
    int scrollOffset = 0;
    Cursor cursor;

    public Viewport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Point getCursorPositionForDisplay() {

        Point combinedPosition = getCombinedPosition();
        Point cpos = new Point();
        cpos.x = combinedPosition.x + cursor.x;
        cpos.y = combinedPosition.y - scrollOffset + cursor.y;
        return cpos;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        cursor = new Cursor(textBuffer);
    }

    // if cursor goes off screen scroll to show it.
    public void scrollToCursor() {
        int scrollPad = 5;
//        if (cursor.y>scrollOffset+height-scrollPad) {
//            scrollOffset = cursor.y-height+scrollPad;
//        };
        if (scrollOffset < cursor.y - height + scrollPad) {
            scrollOffset = cursor.y - height + scrollPad;
        }
        if (scrollOffset > cursor.y - scrollPad) {
            scrollOffset = cursor.y - scrollPad;
            if (scrollOffset < 0) scrollOffset = 0;
        }
    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;

        // hack
        scrollToCursor();

        ColorRepo.setNormalTextColor(tg);
        Point panelPos = getCombinedPosition();
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');

        int usableWidth = width;

        int lineCount = textBuffer.getLineCount();
        String currentLine = "";
        for (int i = 0; i < height; i++) {
            if (i < lineCount) {
                currentLine = textBuffer.getLine(i + scrollOffset);
            } else {
                currentLine = ".";
            }

            if (currentLine.length() > usableWidth)
                currentLine = currentLine.substring(0, usableWidth);

            tg.putString(panelX, panelY + i, currentLine);
        }

    }
}
