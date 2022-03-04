package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.TextBuffer;

// Text editor panel
public class EditorPanel extends Panel {

    int scrollPad = 2;

    TextBuffer textBuffer;
    int vScrollOffset = 0;
    Cursor cursor;
    MainApp mainApp;

    int leftMarginSize = 2;

    boolean wrap=true;

    public EditorPanel(MainApp mainApp) {
        this.setVisible(false);
        this.mainApp = mainApp;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Point getCursorPositionForDisplay() {
        Point combinedPosition = getCombinedPosition();
        Point cpos = new Point();
        cpos.x = combinedPosition.x + cursor.x + leftMarginSize;
        cpos.y = combinedPosition.y - vScrollOffset + cursor.y;
        return cpos;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        cursor = new Cursor(textBuffer);
    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;
        if (!visible) return;

        // hack
        scrollToCursor();

        // ColorRepo.setNormalTextColor(tg);
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.NORMAL_TEXT);

        Point panelPos = getCombinedPosition();
        if (size.x > 0 && size.y > 0)
            Utilities.fillRectangle(tg, panelPos.x, panelPos.y, size.x, size.y, ' ');


        int usableWidth = size.x-leftMarginSize;

        int lineCount = textBuffer.getLineCount();
        String lineText;
        int i=0;
        //for (int i = 0; i < size.y; i++) {
        while (i<size.y) {
            if (i < lineCount) {
                lineText = textBuffer.getLine(i + vScrollOffset);
            } else {
                lineText = ".";
            }

            int lineHeight = calculateHeightofLine(lineText, usableWidth);

            if (lineText.length() > usableWidth)
                lineText = lineText.substring(0, usableWidth);

            String sanitizedText = sanitizeText(lineText);

            tg.putString(panelPos.x+leftMarginSize, panelPos.y + i, sanitizedText);

            i+=lineHeight;
        }

    }

//    public void renderLineWrapped(String lineText, int x, int y, int usableWidth) {
//        int seek=0;
//        int textLength = lineText.length();
//
//        String subString =
//    }

    public int calculateHeightofLine(String text, int usableWidth) {
        if (wrap==false) return 1;
return 1;
//        int height = text.length()/usableWidth;
//        if (text.length()%usableWidth>0) height++;
//        if (height<1) height=1;
//        return height;
    }

    public void renderLine() {
    }

    public String sanitizeText(String input) {
        return input.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
    }

    // if cursor goes off screen scroll to show it.
    public void scrollToCursor() {

        if (vScrollOffset < cursor.y - size.y + scrollPad) {
            vScrollOffset = cursor.y - size.y + scrollPad;
        }
        if (vScrollOffset > cursor.y - scrollPad) {
            vScrollOffset = cursor.y - scrollPad;
            if (vScrollOffset < 0) vScrollOffset = 0;
        }
    }

    public int getVScrollOffset() {
        return vScrollOffset;
    }

    // TODO: this needs to account for line wrapping
    public int getDocumentLineCount() {
        return textBuffer.getLineCount();
    }

}
