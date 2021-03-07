package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;

public class InfoBar extends Panel {
    MainApp editorFrame;

    public InfoBar(MainApp editorFrame) {
        this.editorFrame = editorFrame;
    }

    @Override
    public void draw(TextGraphics tg) {
        ColorRepo.setInfoBarTextColor(tg);
        drawBackground(tg);

        Viewport viewport = editorFrame.getActiveViewport();

        Cursor curser = viewport.getCursor();

        int y = curser.y;
        int x = curser.x;

        String strCoords = "[" + x + "," + y + "]";

        Point pos = getCombinedPosition();

        tg.putString(pos.x + 1, pos.y, strCoords);

        // node info
        int nodeCount = ((PieceTableTextBuffer) editorFrame.textBuffer).getNodes().size();
        tg.putString(pos.x + 20, pos.y, "nodes:" + nodeCount);
    }

    public void drawBackground(TextGraphics tg) {
        Point panelPos = getCombinedPosition();
//        TerminalPosition pos = new TerminalPosition(panelPos.x, panelPos.y);
//        TerminalSize size = new TerminalSize(width, height);
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');
//        tg.fillRectangle(pos, size, '.');
    }
}
