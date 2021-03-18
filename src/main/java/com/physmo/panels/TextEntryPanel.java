package com.physmo.panels;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.physmo.Point;

public class TextEntryPanel extends Panel {

    boolean showBorders = true;
    String text;
    int cursorPosition;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        cursorPosition = text.length();
    }

    @Override
    protected void draw(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.RED);

        Point cp = getCombinedPosition();
        tg.putString(cp.x, cp.y, text);

        if (hasFocus()) {
            tg.setForegroundColor(TextColor.ANSI.RED);
            tg.setBackgroundColor(TextColor.ANSI.WHITE);
            char cursorChar = ' ';
            if (cursorPosition >= 0 && cursorPosition < text.length()) cursorChar = text.charAt(cursorPosition);
            tg.putString(cp.x + cursorPosition, cp.y, "" + cursorChar);
        }
    }

    @Override
    protected boolean processKeystroke(KeyStroke keyStroke) {
        super.processKeystroke(keyStroke);

        if (keyStroke.getKeyType() == KeyType.Character) {
            Character character = keyStroke.getCharacter();
            insertChar(character);
        }

        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            cursorPosition--;
            if (cursorPosition < 0) cursorPosition = 0;
        }

        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            cursorPosition++;
            if (cursorPosition >= text.length()) cursorPosition = text.length();
        }

        return false;
    }

    public void insertChar(char c) {
        if (cursorPosition == 0) {
            text = c + text;
            cursorPosition++;
        } else if (cursorAtEndOfText()) {
            text = text + c;
            cursorPosition++;
        } else {
            String suba = text.substring(0, cursorPosition);
            String subb = text.substring(cursorPosition);
            text = suba + c + subb;
            cursorPosition++;
        }
    }

    public boolean cursorAtEndOfText() {
        return cursorPosition == text.length();
    }
}
