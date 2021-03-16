package com.physmo.panels;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;

public class ScrollBar extends Panel {
    Viewport parentViewport;
    MainApp mainApp;

    public ScrollBar(MainApp mainApp, Viewport parentViewport) {
        this.parentViewport = parentViewport;
        this.mainApp = mainApp;
    }

    @Override
    public void draw(TextGraphics tg) {
        char charTrack = Symbols.BLOCK_MIDDLE;
        char charSlider = Symbols.BLOCK_SOLID;

        Point pos = getCombinedPosition();
        //ColorRepo.setScrollBarTextColor(tg);
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.NORMAL_TEXT);
        Utilities.fillRectangle(tg, pos.x, pos.y, size.x, size.y, charTrack);

        int documentLines = parentViewport.getTextPanel().getDocumentLineCount();
        int textWindowHeight = parentViewport.getTextPanel().getSize().y;
        int scrollOffset = parentViewport.getTextPanel().getScrollOffset();

        double ratio = (double) textWindowHeight / (double) (documentLines + textWindowHeight);
        int barSize = (int) ((double) textWindowHeight * ratio);
        if (barSize < 1) barSize = 1;
        int barPos = (int) ((double) (textWindowHeight - barSize) * ((double) scrollOffset / (double) documentLines));

        Utilities.fillRectangle(tg, pos.x, pos.y + barPos, size.y, barSize, charSlider);

    }

}
