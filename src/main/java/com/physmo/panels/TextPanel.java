package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.TextBuffer;

// TODO: rename to text viewport?
public class TextPanel extends Panel {

    int scrollPad = 2;

    TextBuffer textBuffer;
    int scrollOffset = 0;
    Cursor cursor;


    public TextPanel() {
        this.setVisible(false);
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
        if (visible == false) return;

        // hack
        scrollToCursor();

        ColorRepo.setNormalTextColor(tg);
        Point panelPos = getCombinedPosition();
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');

        //Utilities.fillRectangle(tg, 10, 5, 2, 2, 'n');

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

            tg.putString(panelPos.x, panelPos.y + i, currentLine);
        }

    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getDocumentLineCount() {
        return textBuffer.getLineCount();
    }

}
