package com.physmo.editorpanel;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.CursorMetricSupplier;
import com.physmo.LineSplitter;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.TextBuffer;
import com.physmo.panels.Panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Text editor panel
public class EditorPanel extends Panel implements CursorMetricSupplier {

    int scrollPad = 2;

    TextBuffer textBuffer;
    int vScrollOffset = 0; // This is in sub lines not line numbers.
    Cursor cursor;
    MainApp mainApp;

    int leftMarginSize = 4;
    int totalLineHeight = 0;

    boolean wrap = true;

    boolean dirty = false;

    LineSplitter lineSplitter;
    List<Block> blockList = new ArrayList<>();

    int tabSize=8;

    char tabSpecialChar = Symbols.ARROW_RIGHT;
    char weirdChar = Symbols.CLUB;


    public EditorPanel(MainApp mainApp) {
        this.setVisible(false);
        this.mainApp = mainApp;


    }

    public void notifyChanged() {
        dirty=true;
        if (dirty) {
            refreshBlockList();
            dirty=false;
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Point getCursorPositionForDisplay() {
        Point combinedPosition = getCombinedPosition();
        Point cpos = new Point();

        int xWithTabs = translateLineXPosition(cursor.y, cursor.x);

        cpos.x = combinedPosition.x + xWithTabs + leftMarginSize;
        cpos.y = combinedPosition.y - vScrollOffset + cursor.y;
        return cpos;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        dirty=true;
        cursor = new Cursor(this);


    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;
        if (!visible) return;

        // Put this here for now, find a more efficient place to recompute all blocks later.
        if (dirty) {
            refreshBlockList();
            dirty=false;
        }

        Map<Integer, String> lineCache = fetchVisibleLineCache(blockList);

        // hack
        scrollToCursor();

        // ColorRepo.setNormalTextColor(tg);
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.NORMAL_TEXT);

        Point panelPos = getCombinedPosition();
        if (size.x > 0 && size.y > 0)
            Utilities.fillRectangle(tg, panelPos.x, panelPos.y, size.x, size.y, ' ');


        int line = findLineNumberFromSubLine(blockList, vScrollOffset);
        int subLine = findSubLineOffsetInBlockList(blockList, vScrollOffset);
        boolean good = true;
        int x = panelPos.x + leftMarginSize;
        int y = panelPos.y;
        while (good) {
            if (line >= blockList.size()) break;
            while (subLine < blockList.get(line).height) {
                drawMargin(tg, line, subLine, panelPos.x, y);
                drawSubLine(tg, blockList, lineCache, line, subLine, x, y++);
                subLine++;
            }
            subLine = 0;
            line++;
        }

    }

    public void drawMargin(TextGraphics tg, int lineNumber, int subLine, int x, int y) {
        if (subLine == 0) {
            tg.putString(x, y, "" + lineNumber);
        }
    }

    public void drawSubLine(TextGraphics tg, List<Block> bl, Map<Integer, String> lineCache, int lineNumber, int subLine, int x, int y) {

        String line = lineCache.get(lineNumber);
        if (line == null) return;

        Block block = bl.get(lineNumber);
        int subLineStart = block.split[subLine * 3];
        int subLineLength = block.split[(subLine * 3) + 1];
        String subText = line.substring(subLineStart, subLineStart + subLineLength);

        tg.putString(x, y, sanitizeText(subText, tabSize));


    }



    public String convertTabCharacters(String input, int tabSize) {
        String tabChars=tabSpecialChar+"                   ";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0;i<input.length();i++) {
            char c = input.charAt(i);
            if (c=='\t') {
                int padSize = tabSize-(stringBuilder.length()%tabSize);
                stringBuilder.append(tabChars.substring(0,padSize));
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

        return input.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", ""+weirdChar);
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
        return totalLineHeight;
    }

    public void refreshBlockList() {
        int usableWidth = size.x - leftMarginSize;
        //System.out.println("usable width:" + usableWidth);
        lineSplitter = new LineSplitter(usableWidth, 0);

        blockList.clear();
        BlockProcessor blockProcessor = new BlockProcessor();
        blockList = blockProcessor.processAll(textBuffer, lineSplitter);
        countTotalLines();
    }

    // Add up all sub lines.
    public void countTotalLines() {
        totalLineHeight = 0;
        for (Block block : blockList) {
            totalLineHeight += block.height;
        }
    }

    public Map<Integer, String> fetchVisibleLineCache(List<Block> bl) {
        Map<Integer, String> cache = new HashMap<>();
        // vScrollOffset
        int lineCount = textBuffer.getLineCount();
        int startingLine = findLineNumberFromSubLine(bl, vScrollOffset);

        // TODO: use the real required height here.
        for (int i = 0; i < 40; i++) {
            int lineNumber = startingLine + i;
            if (lineNumber < lineCount) {
                cache.put(lineNumber, textBuffer.getLine(lineNumber));
            }
        }

        return cache;
    }

    // Returns line number that contains the subLine
    public int findLineNumberFromSubLine(List<Block> bl, int subLine) {
        int accumulator = 0;

        // Scan lines to find where the vScrollOffset starts (could be a sub-line)
        for (Block block : blockList) {
            if (accumulator > subLine) return block.lineNumber - 1;
            accumulator += block.height;
        }

        return 0;
    }

    // returns the line offset inside the block for the global subline
    public int findSubLineOffsetInBlockList(List<Block> bl, int subLine) {
        int accumulator = 0;

        // Scan lines to find where the vScrollOffset starts (could be a sub-line)
        for (Block block : blockList) {
            if (accumulator + block.height > subLine) {
                return subLine - (accumulator);
            }
            accumulator += block.height;
        }

        return 0;
    }

    @Override
    public int getLineLength(int lineNumber) {
        int subLineInBlockList = findLineNumberFromSubLine(blockList, lineNumber);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList(blockList, lineNumber);
        Block block = blockList.get(subLineInBlockList);
        int length = block.split[(subLineOffsetInBlockList * 3) + 1]; // expanded length
        return length;
    }

    @Override
    public int getTotalLines() {
        return totalLineHeight;
    }

    @Override
    public int getStartOfLineIndex(int lineNumber) {
        int subLineInBlockList = findLineNumberFromSubLine(blockList, lineNumber); // get the major line number.
        int charIndex = textBuffer.getStartOfLineIndex(subLineInBlockList);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList(blockList, lineNumber);

        Block block = blockList.get(subLineInBlockList);

        // scan through and count chars in each sub line
        for (int i = 0; i < subLineOffsetInBlockList; i++) {
            charIndex += block.split[(i * 3) + 1];
        }

        return charIndex;
    }

    // Take the raw x position on a line and translate it taking
    // into account expanded tabs if they are active.
    public int translateLineXPosition(int lineNumber, int x) {
        int subLineInBlockList = findLineNumberFromSubLine(blockList, lineNumber);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList(blockList, lineNumber);
        Block block = blockList.get(subLineInBlockList);
        int firstCharInLine = block.split[(subLineOffsetInBlockList * 3) + 0];

        String textBufferLine = textBuffer.getLine(subLineInBlockList);
        int tabAdjust=0;

        // count tabs in line section before X position
        for (int i=firstCharInLine;i<firstCharInLine + x;i++) {
            if (textBufferLine.charAt(i)=='\t') {
                // calculate how many characters we would need to add to get to the next tab column.
                int pad = tabSize-((i+tabAdjust)%tabSize);
                tabAdjust+=pad-1;
            }
        }

        return x+tabAdjust;
    }

    public String getCharacterUnderCursor() {

        String character = textBuffer.getCharacter(cursor.getDocumentIndex());
        return character;
    }
}
