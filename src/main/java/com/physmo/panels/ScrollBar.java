package com.physmo.panels;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Point;
import com.physmo.Utilities;

public class ScrollBar extends Panel {
    Viewport parentViewport;

    public ScrollBar(Viewport parentViewport) {
        this.parentViewport = parentViewport;
    }

    @Override
    public void draw(TextGraphics tg) {
        char charTrack = Symbols.DOUBLE_LINE_VERTICAL;
        char charSlider = Symbols.BLOCK_DENSE;

        Point pos = getCombinedPosition();
        ColorRepo.setScrollBarTextColor(tg);
        Utilities.fillRectangle(tg, pos.x, pos.y, width, height, charTrack);

        int documentLines = parentViewport.getTextPanel().getDocumentLineCount();
        int textWindowHeight = parentViewport.getTextPanel().getHeight();
        int scrollOffset = parentViewport.getTextPanel().getScrollOffset();

        double ratio = (double) textWindowHeight / (double) (documentLines + textWindowHeight);
        int barSize = (int) ((double) textWindowHeight * ratio);
        if (barSize < 1) barSize = 1;
        int barPos = (int) ((double) (textWindowHeight - barSize) * ((double) scrollOffset / (double) documentLines));

        Utilities.fillRectangle(tg, pos.x, pos.y + barPos, width, barSize, charSlider);

//        ColorRepo.setInfoBarTextColor(tg);
//        drawBackground(tg);
//
//        Viewport viewport = editorFrame.getActiveViewport();
//
//        Cursor curser = viewport.getCursor();
//
//        int y = curser.y;
//        int x = curser.x;
//
//        String strCoords = "[" + x + "," + y + "]";
//
//        Point pos = getCombinedPosition();
//
//        tg.putString(pos.x + 1, pos.y, strCoords);
//
//        // node info
//        int nodeCount = ((PieceTableTextBuffer) editorFrame.textBuffer).getNodes().size();
//        tg.putString(pos.x + 20, pos.y, "nodes:" + nodeCount);
//
//        int w = editorFrame.getActiveViewport().getWidth();
//        int h = editorFrame.getActiveViewport().getHeight();
//        tg.putString(pos.x + 40, pos.y, ""+w+","+h);
    }

    public void drawBackground(TextGraphics tg) {
//        Point panelPos = getCombinedPosition();
////        TerminalPosition pos = new TerminalPosition(panelPos.x, panelPos.y);
////        TerminalSize size = new TerminalSize(width, height);
//        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');
////        tg.fillRectangle(pos, size, '.');
    }
}
