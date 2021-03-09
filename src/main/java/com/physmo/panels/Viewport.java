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
        textPanel.setParent(this);

        scrollBar = new ScrollBar(mainApp, this);
    }

    public TextPanel getTextPanel() {
        return textPanel;
    }

    public void doLayout() {

        textPanel.setParent(this);
        textPanel.setPanelX(0);
        textPanel.setPanelY(0);
        textPanel.setWidth(this.width - 1);
        textPanel.setHeight(this.height);
        textPanel.setVisible(true);


        scrollBar.setParent(this);
        scrollBar.setPanelX(this.width - 1);
        scrollBar.setPanelY(0);
        scrollBar.setWidth(1);
        scrollBar.setHeight(this.height);
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
