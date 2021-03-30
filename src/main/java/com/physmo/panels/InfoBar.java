package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
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

        int[] cursorCoords = getCursorCoords();

        String strCoords = "[" + cursorCoords[0] + "," + cursorCoords[1] + "]";

        Point pos = getCombinedPosition();

        tg.putString(pos.x + 1, pos.y, strCoords);

        // node info
        mainApp.getActiveViewport().ifPresent(vp -> {
            int nodeCount = ((PieceTableTextBuffer) vp.getTextBuffer()).getNodes().size();
            tg.putString(pos.x + 20, pos.y, "nodes:" + nodeCount);
        });

        int[] viewportSize = getViewportSize();
        tg.putString(pos.x + 40, pos.y, "" + viewportSize[0] + "," + viewportSize[1]);
    }

    public int[] getCursorCoords() {
        int[] vals = {0, 0};
        mainApp.getActiveViewport().ifPresent(vp -> {
            vals[0] = vp.getCursor().x;
            vals[1] = vp.getCursor().y;
        });
        return vals;
    }

    public int[] getViewportSize() {
        int[] vals = {0, 0};
        mainApp.getActiveViewport().ifPresent(vp -> {
            vals[0] = vp.getSize().x;
            vals[1] = vp.getSize().y;
        });
        return vals;
    }

    public void drawBackground(TextGraphics tg) {
        Point panelPos = getCombinedPosition();
        Utilities.fillRectangle(tg, panelPos.x, panelPos.y, size.x, size.y, ' ');
    }
}
