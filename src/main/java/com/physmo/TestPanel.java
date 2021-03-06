package com.physmo;

import com.googlecode.lanterna.graphics.TextGraphics;

public class TestPanel extends Panel {

    @Override
    void draw(TextGraphics tg) {
        Point pos = getCombinedPosition();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tg.putString(pos.x+x,pos.y+y,"*");
            }
        }

    }
}
