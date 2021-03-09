package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;

public class InfoBar extends Panel {
    MainApp mainApp;

    public InfoBar(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void draw(TextGraphics tg) {
        //ColorRepo.setInfoBarTextColor(tg);
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.INFO_BAR);

        drawBackground(tg);

        Viewport viewport = mainApp.getActiveViewport();

        Cursor curser = viewport.getCursor();

        int y = curser.y;
        int x = curser.x;

        String strCoords = "[" + x + "," + y + "]";

        Point pos = getCombinedPosition();

        tg.putString(pos.x + 1, pos.y, strCoords);

        // node info
        int nodeCount = ((PieceTableTextBuffer) mainApp.textBuffer).getNodes().size();
        tg.putString(pos.x + 20, pos.y, "nodes:" + nodeCount);

        int w = mainApp.getActiveViewport().getWidth();
        int h = mainApp.getActiveViewport().getHeight();
        tg.putString(pos.x + 40, pos.y, "" + w + "," + h);
    }

    public void drawBackground(TextGraphics tg) {
        Point panelPos = getCombinedPosition();
//        TerminalPosition pos = new TerminalPosition(panelPos.x, panelPos.y);
//        TerminalSize size = new TerminalSize(width, height);
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, width, height, ' ');
//        tg.fillRectangle(pos, size, '.');
    }
}
