package com.physmo;

import com.physmo.buffers.piecetable.PieceTableTextBuffer;
import com.physmo.editorpanel.Block;
import com.physmo.editorpanel.BlockProcessor;
import org.junit.Test;

import java.util.List;

public class TestBlockProcessor {
    @Test
    public void t1() {
        LineSplitter ls = new LineSplitter(10,0);
        int[] splits = ls.split("111111111122222222223333333333444", 8);

        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialText("12345678901234567890\naaaaaaaaaaaaaaaabbbbbbbbbbbbbb");

        BlockProcessor blockProcessor = new BlockProcessor();
        List<Block> blocks = blockProcessor.processAll(pt, ls,8);

    }
}
