package com.physmo.editorpanel;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.TextBuffer;
import com.physmo.panels.Panel;

import java.util.Map;

// Text editor panel
public class EditorPanel extends Panel {

    int scrollPad = 2;

    TextBuffer textBuffer;
    int vScrollOffset = 0; // This is in sub lines not line numbers.
    Cursor cursor;
    MainApp mainApp;

    int leftMarginSize = 4;
    int tabSize = 8;

    char tabSpecialChar = Symbols.TRIANGLE_RIGHT_POINTING_BLACK;
    char weirdChar = Symbols.CLUB;

    LineProcessor lineProcessor = new LineProcessor(null);


    public EditorPanel(MainApp mainApp) {
        this.setVisible(false);
        this.mainApp = mainApp;

    }

    public void notifyChanged() {
        dirty = true;

        lineProcessor.setDirty();

    }

    public Cursor getCursor() {
        return cursor;
    }

    public Point getCursorPositionForDisplay() {
        Point combinedPosition = getCombinedPosition();
        Point cpos = new Point();

        int xWithTabs = lineProcessor.translateLineXPosition(cursor.y, cursor.x);

        cpos.x = combinedPosition.x + xWithTabs + leftMarginSize;
        cpos.y = combinedPosition.y - vScrollOffset + cursor.y;
        return cpos;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        dirty = true;
        lineProcessor = new LineProcessor(textBuffer);
        cursor = new Cursor(lineProcessor);


    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;
        if (!visible) return;

        // Put this here for now, find a more efficient place to recompute all blocks later.
//        if (dirty) {
//            lineProcessor.refreshBlockList();
//            dirty = false;
//        }
        // hack
        scrollToCursor();
        leftMarginSize = 1+ calculateMarginSizeForTotalLines(lineProcessor.getTotalLines());
        lineProcessor.setVisibleWidth(this.getSize().x - leftMarginSize - 1);
        lineProcessor.setVisibleHeight(this.getSize().y - 1);
        lineProcessor.setVScrollOffset(vScrollOffset);
        lineProcessor.refreshIfDirty();

        //Map<Integer, String> lineCache = lineProcessor.fetchVisibleLineCache2(vScrollOffset);


        // ColorRepo.setNormalTextColor(tg);
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.NORMAL_TEXT);

        Point panelPos = getCombinedPosition();
        if (size.x > 0 && size.y > 0)
            Utilities.fillRectangle(tg, panelPos.x, panelPos.y, size.x, size.y, ' ');


        //int line = lineProcessor.findLineNumberFromSubLine(vScrollOffset);
        //int subLine = lineProcessor.findSubLineOffsetInBlockList(vScrollOffset);
        //boolean good = true;
        int x = panelPos.x + leftMarginSize;
        int y = panelPos.y;
        Map<Integer, String> expandedLineCache = lineProcessor.getExpandedLineCache();
        Map<Integer, Integer> marginCache = lineProcessor.getMarginCache();
        int visibleHeight = getSize().y;
        for (int i = 0; i < visibleHeight; i++) {
            String text = expandedLineCache.get(vScrollOffset + i);
            if (text == null) continue;
            tg.putString(x, y + i, sanitizeText(text, tabSize));
            tg.putString(panelPos.x, y + i, buildMarginString(leftMarginSize,marginCache.get(vScrollOffset + i)));
            //drawMargin2(tg, leftMarginSize,marginCache.get(vScrollOffset + i), 0, y + i);
        }

    }

    public void drawMargin(TextGraphics tg, int lineNumber, int subLine, int x, int y) {
        if (subLine == 0) {
            tg.putString(x, y, "" + lineNumber);
        }
    }

    public String buildMarginString(int marginWidth, int marginNumber) {

        String spaces = "              ";

        if (marginNumber==-1) return spaces.substring(0,marginWidth);

        int n = marginNumber;
        int count = marginNumber==0?2:1;
        while (n>0) {
            n=n/10;
            count++;
        }


        return spaces.substring(0,marginWidth-count) + marginNumber;
    }

    // TODO: create test for this.
    public int calculateMarginSizeForTotalLines(int totalLines) {
        if (totalLines==0) return 2;
        int count=0;
        while (totalLines!=0) {
            totalLines/=10;
            count++;
        }
        return count;
    }


    public String convertTabCharacters(String input, int tabSize) {
        String tabChars = tabSpecialChar + "                   ";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\t') {
                int padSize = tabSize - (stringBuilder.length() % tabSize);
                stringBuilder.append(tabChars.substring(0, padSize));
            } else {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }

    public String sanitizeText(String input, int tabSize) {

        if (input.contains("\t")) {
            input = convertTabCharacters(input, tabSize);
        }

        return input.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "" + weirdChar);
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
        return lineProcessor.getTotalLines();
    }

    public int getCostlyFunctionCallCount() {
        return 0;//this.getLineLengthCallCount;
    }

    public String getCharacterUnderCursor() {

        String character = textBuffer.getCharacter(cursor.getDocumentIndex());
        return character;
    }
}
