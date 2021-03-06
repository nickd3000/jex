package com.physmo;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;

public class InfoBar extends Panel {
    EditorFrame editorFrame;

    public InfoBar(EditorFrame editorFrame) {
        this.editorFrame = editorFrame;
    }

    @Override
    void draw(TextGraphics tg) {
        ColorRepo.setInfoBarTextColor(tg);
        drawBackground(tg);

        Viewport viewport = editorFrame.testViewport;

        Cursor curser = viewport.getCurser();

        int y = curser.y;
        int x = curser.x;

        String strCoords = "[" + x + "," + y + "]";

        Point pos = getCombinedPosition();

        tg.putString(pos.x, pos.y, strCoords);
    }

    public void drawBackground(TextGraphics tg) {
        Point panelPos = getCombinedPosition();
//        TerminalPosition pos = new TerminalPosition(panelPos.x, panelPos.y);
//        TerminalSize size = new TerminalSize(width, height);
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');
//        tg.fillRectangle(pos, size, '.');
    }
}
