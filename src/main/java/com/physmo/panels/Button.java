package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Point;

public class Button extends Panel {
    String text;

    @Override
    protected void draw(TextGraphics tg) {
        Point cp = getCombinedPosition();
        tg.putString(cp.x, cp.y, text);
    }
}
