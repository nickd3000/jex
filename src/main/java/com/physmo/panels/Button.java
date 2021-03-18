package com.physmo.panels;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Point;

import java.util.HashMap;
import java.util.Map;

public class Button extends Panel {
    String text;
    Map<String, TextColor> colorMap;

    public Button() {
        colorMap = new HashMap<>();
        colorMap.put("FG", TextColor.ANSI.RED);
        colorMap.put("BG", TextColor.ANSI.WHITE);
    }

    @Override
    protected void draw(TextGraphics tg) {

        if (hasFocus()) {
            tg.setForegroundColor(colorMap.get("FG"));
            tg.setBackgroundColor(colorMap.get("BG"));
        } else {
            tg.setForegroundColor(colorMap.get("BG"));
            tg.setBackgroundColor(colorMap.get("FG"));
        }

        Point cp = getCombinedPosition();
        tg.putString(cp.x, cp.y, text);
    }
}
