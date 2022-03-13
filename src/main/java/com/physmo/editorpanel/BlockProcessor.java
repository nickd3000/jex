package com.physmo.editorpanel;

import com.physmo.LineSplitter;
import com.physmo.buffers.TextBuffer;

import java.util.ArrayList;
import java.util.List;

// Create a list of blocks that represent the metrics of line-wrapped lines.
public class BlockProcessor {

    public List<Block> processAll(TextBuffer textBuffer, LineSplitter lineSplitter, int tabSize) {
        List<Block> blocks = new ArrayList<>();
        int lineCount = textBuffer.getLineCount();

        for (int i = 0; i < lineCount; i++) {
            blocks.add(createBlockFromLine(lineSplitter, textBuffer.getLine(i), i, tabSize));
        }

        return blocks;
    }

    public Block createBlockFromLine(LineSplitter lineSplitter, String line, int lineNumber, int tabSize) {
        Block block = new Block();
        block.lineNumber = lineNumber;
        int[] split = lineSplitter.split(line, tabSize);
        block.height = split.length / 3;
        block.split = split;
        return block;
    }

}
