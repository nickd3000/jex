package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

        char characterAtCursor = getCharacterAtCursor();
        tg.putString(pos.x + 10, pos.y, ""+(0+characterAtCursor));

        tg.putString(pos.x + 16, pos.y, ""+(getCostlyFunctionCallCount()));


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

    public char getCharacterAtCursor() {
        AtomicReference<Character> c= new AtomicReference<>(' ');
        mainApp.getActiveViewport().ifPresent(vp -> {
            String characterUnderCursor = vp.getEditorPanel().getCharacterUnderCursor();
            if (characterUnderCursor.length()==1) {
                c.set(characterUnderCursor.charAt(0));
            }

        });
        return c.get();
    }

    public int getCostlyFunctionCallCount() {
        Optional<Viewport> activeViewport = mainApp.getActiveViewport();
        if (activeViewport.isPresent()) {
            return activeViewport.get().getEditorPanel().getCostlyFunctionCallCount();
        }
        return 0;
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
