package com.physmo.editorpanel;

import com.physmo.CursorMetricSupplier;
import com.physmo.LineSplitter;
import com.physmo.buffers.TextBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SubmissionPublisher;

/*
    line cache
    handle wrapping
 */
public class LineProcessor implements CursorMetricSupplier {

    TextBuffer textBuffer;
    private int visibleWidth = 40;
    private int visibleHeight = 40;
    LineSplitter lineSplitter;
    List<Block> blockList = new ArrayList<>();
    int tabSize=8;

    Map<Integer, String> lineCache = new HashMap<>();; // Raw line data
    Map<Integer, String> expandedLineCache = new HashMap<>(); // Raw sub lines - no expanded tabs
    Map<Integer, Integer> marginCache = new HashMap<>();
    private int dirtyLineNumber;
    private boolean lineDirty = false;

    public Map<Integer, String> getExpandedLineCache() {
        return expandedLineCache;
    }
    public Map<Integer, Integer> getMarginCache() {
        return marginCache;
    }

    boolean dirty = false;

    int totalLineHeight = 0;
    int vScrollOffset = 0;


    public LineProcessor(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        dirty=true;
    }

    public void refreshIfDirty() {
        if (dirty) {
            System.out.println("it's dirty");
            refreshBlockList();
            dirty = false;
        }
        if (lineDirty) {
            System.out.println("it's line dirty");
            refreshLine(dirtyLineNumber);
            lineDirty=false;
        }
    }

    public void setDirty() {
        dirty=true;
    }

    public void setDirty(int lineNumber) {
        lineDirty=true;
        dirtyLineNumber=lineNumber;
        refreshIfDirty();
    }

    public void setVisibleWidth(int visibleWidth) {
        if (this.visibleWidth != visibleWidth) {
            this.visibleWidth = visibleWidth;
            dirty = true;
        }
    }
    public void setVisibleHeight(int visibleHeight) {
        if (this.visibleHeight != visibleHeight) {
            this.visibleHeight = visibleHeight;
            dirty = true;
        }
    }
    public void setVScrollOffset(int vScrollOffset) {
        if (this.vScrollOffset!=vScrollOffset) {
            this.vScrollOffset = vScrollOffset;
            dirty=true;
        }
    }

    public void refreshBlockList() {

        lineSplitter = new LineSplitter(visibleWidth, 0);

        blockList.clear();
        BlockProcessor blockProcessor = new BlockProcessor();
        blockList = blockProcessor.processAll(textBuffer, lineSplitter, tabSize);
        countTotalLines();
        fetchVisibleLineCache(vScrollOffset);

    }

    public void refreshLine(int lineNumber) {
        lineSplitter = new LineSplitter(visibleWidth, 0);

        //blockList.clear();
        BlockProcessor blockProcessor = new BlockProcessor();

        //blockList = blockProcessor.processAll(textBuffer, lineSplitter, tabSize);
        Block newBlock = blockProcessor.createBlockFromLine(lineSplitter, textBuffer, lineNumber, tabSize);

        int blockIndex = -1;
        for (int i=0;i<blockList.size();i++) {
            if (blockList.get(i).lineNumber==lineNumber) {
                blockList.set(i, newBlock);
                break;
            }
        }

        countTotalLines();
        fetchVisibleLineCache(vScrollOffset);
    }


    public Map<Integer, String> fetchVisibleLineCache(int vScrollOffset) {
        lineCache = new HashMap<>();
        expandedLineCache = new HashMap<>();

        // vScrollOffset
        int lineCount = textBuffer.getLineCount();
        int startingLine = findLineNumberFromSubLine( vScrollOffset);

        // TODO: use the real required height here.
        for (int i = 0; i < visibleHeight; i++) {
            int lineNumber = startingLine + i;
            if (lineNumber < lineCount) {
                lineCache.put(lineNumber, textBuffer.getLine(lineNumber));
            }
        }



        // Pre calculate expanded sub lines.
        for (int i=0;i<visibleHeight;i++) {
            int lineNumber = vScrollOffset + i;
            if (lineNumber < totalLineHeight && !expandedLineCache.containsKey(lineNumber)) {
                expandedLineCache.put(lineNumber, calculateExpandedSubLine(blockList, lineCache, lineNumber ));
                marginCache.put(lineNumber, calculateMarginLineNumberForSubLine(lineNumber));
            }
        }

        return lineCache;
    }

    public String calculateExpandedSubLine(List<Block> bl, Map<Integer, String> lineCache, int subLine) {

        int lineNumber = findLineNumberFromSubLine(subLine);
        int lineOffset = findSubLineOffsetInBlockList(subLine);
        String line = lineCache.get(lineNumber);
        if (line == null) return "";

        Block block = bl.get(lineNumber);
        int subLineStart = block.split[lineOffset * 3];
        int subLineLength = block.split[(lineOffset * 3) + 1];
        String subText = line.substring(subLineStart, subLineStart + subLineLength);
        return subText;

    }

    // Add up all sub lines.
    public void countTotalLines() {
        totalLineHeight = 0;
        for (Block block : blockList) {
            totalLineHeight += block.height;
        }
    }

    @Override
    public int getTotalLines() {
        return totalLineHeight;
    }


    @Override
    public int getLineLength(int lineNumber) {

        int subLineInBlockList = findLineNumberFromSubLine( lineNumber);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList( lineNumber);
        Block block = blockList.get(subLineInBlockList);
        int length = block.split[(subLineOffsetInBlockList * 3) + 1]; // expanded length
        return length;
    }

    @Override
    public int getMajorLineNumber(int y) {
       return findLineNumberFromSubLine(y);
    }

    @Override
    public int getStartOfLineIndex(int lineNumber) {
        if (blockList.size()==0) return 0;

        int subLineInBlockList = findLineNumberFromSubLine( lineNumber); // get the major line number.
        int charIndex = textBuffer.getStartOfLineIndex(subLineInBlockList);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList(lineNumber);

        Block block = blockList.get(subLineInBlockList);

        // scan through and count chars in each sub line
        for (int i = 0; i < subLineOffsetInBlockList; i++) {
            charIndex += block.split[(i * 3) + 1];
        }

        return charIndex;
    }




    // Returns major line number that contains the subLine
    public int findLineNumberFromSubLine(int subLine) {
        int accumulator = 0;

        // Find what major line this sub line exists in.
        for (Block block : blockList) {
            if (accumulator+block.height > subLine) return block.lineNumber;
            accumulator += block.height;
        }


        // would we ever want this to return 0?
        return 0;
    }

    // returns the line offset inside the block for the global subline
    public int findSubLineOffsetInBlockList( int subLine) {
        int accumulator = 0;

        // Scan lines to find where subLine lies.
        for (Block block : blockList) {
            if (accumulator + block.height > subLine) {
                return subLine - (accumulator);
            }
            accumulator += block.height;
        }

        return 0;
    }

    public int calculateMarginLineNumberForSubLine(int subLine) {
        int lineNumberFromSubLine = findLineNumberFromSubLine(subLine);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList(subLine);
        if (subLineOffsetInBlockList==0) return lineNumberFromSubLine;
        return -1;
    }

    // Take the raw x position on a line and translate it taking
    // into account expanded tabs if they are active.
    public int translateLineXPosition(int lineNumber, int x) {
        int subLineInBlockList = findLineNumberFromSubLine( lineNumber);
        int subLineOffsetInBlockList = findSubLineOffsetInBlockList( lineNumber);
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


}
