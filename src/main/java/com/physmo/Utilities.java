package com.physmo;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Utilities {
    public static void fillRectangle(TextGraphics tg, int x, int y, int width, int height, char c) {
        TerminalPosition pos = new TerminalPosition(x, y);
        TerminalSize size = new TerminalSize(width, height);
        tg.fillRectangle(pos, size, c);
    }
}
