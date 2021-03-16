package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.ColorRepo;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;

public class MenuBar extends Panel {

    MainApp mainApp;

    public MenuBar(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    protected void draw(TextGraphics tg) {
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.MENU_BAR);
        Point panelPos = getCombinedPosition();

        Utilities.fillRectangle(tg, 0, 0, size.x, 1, ' ');
        tg.putString(0, 0, "   File  Edit  Search  Options");
    }

}
