package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Point;

public class TestPanel extends Panel {

    @Override
    public void draw(TextGraphics tg) {
        Point pos = getCombinedPosition();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tg.putString(pos.x + x, pos.y + y, "*");
            }
        }

    }
}
