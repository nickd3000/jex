package com.physmo;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class ColorRepo {
    public static void setDefaultTextColor(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
    }

    public static void setNormalTextColor(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT);
    }

    public static void setInfoBarTextColor(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.GREEN);
    }

    public static void setScrollBarTextColor(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.RED);
    }
}
