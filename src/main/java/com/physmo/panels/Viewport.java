package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.buffers.TextBuffer;

// TODO: rename to text viewport?
// Vieport contains:
//  text panel
//  scroll bar
public class Viewport extends Panel {

    int id;
    TextBuffer textBuffer;
    ScrollBar scrollBar;
    TextPanel textPanel;
    MainApp mainApp;

    public Viewport(MainApp mainApp) {
        this.mainApp = mainApp;
        textPanel = new TextPanel(mainApp);
        addChild(textPanel);

        scrollBar = new ScrollBar(mainApp, this);
        addChild(scrollBar);
    }

    public TextPanel getTextPanel() {
        return textPanel;
    }

    public void doLayout() {
        textPanel.setPosition(0, 0);
        textPanel.setSize(this.size.x - 1, this.size.y);
        textPanel.setVisible(true);

        scrollBar.setPosition(this.size.x - 1, 0);
        scrollBar.setSize(1, this.size.y);
        scrollBar.setVisible(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cursor getCursor() {
        return textPanel.getCursor();
    }

    public Point getCursorPositionForDisplay() {
        return textPanel.getCursorPositionForDisplay();
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        textPanel.setTextBuffer(textBuffer);


    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;
        textPanel.draw(tg);
        scrollBar.draw(tg);
    }
}
