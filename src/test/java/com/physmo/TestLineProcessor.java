package com.physmo;

import com.physmo.buffers.piecetable.PieceTableTextBuffer;
import com.physmo.editorpanel.LineProcessor;
import org.junit.Test;

public class TestLineProcessor {

    @Test
    public void t1() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialText("1111111111 2222222222 3333333333 4444444444\n\n1111111111 2222222222 3333333333 4444444444");

        LineProcessor lineProcessor = new LineProcessor(pt);
        lineProcessor.refreshBlockList();

        lineProcessor.findSubLineOffsetInBlockList(1);
    }
}
