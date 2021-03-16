package com.physmo;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Utilities {
    public static void fillRectangle(TextGraphics tg, int x, int y, int width, int height, char c) {
        TerminalPosition pos = new TerminalPosition(x, y);
        TerminalSize size = new TerminalSize(width, height);
        tg.fillRectangle(pos, size, c);
    }

    public static void drawBox(TextGraphics tg, int x, int y, int width, int height, char c) {
        TerminalPosition pos = new TerminalPosition(x, y);
        TerminalSize size = new TerminalSize(width, height);
        tg.fillRectangle(pos, size, c);

        for (int yy = 0; yy < height; yy++) {

            tg.setCharacter(x, y + yy, Symbols.DOUBLE_LINE_VERTICAL);
            tg.setCharacter(x + width - 1, y + yy, Symbols.DOUBLE_LINE_VERTICAL);
        }
        for (int xx = 0; xx < width; xx++) {
            tg.setCharacter(x + xx, y, Symbols.DOUBLE_LINE_HORIZONTAL);
            tg.setCharacter(x + xx, y + height - 1, Symbols.DOUBLE_LINE_HORIZONTAL);
        }

        tg.setCharacter(x, y, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        tg.setCharacter(x + width - 1, y, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(x + width - 1, y + height - 1, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
        tg.setCharacter(x, y + height - 1, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
    }
}
