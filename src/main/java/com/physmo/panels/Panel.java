package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Point;

// base class for all windows and elements
public abstract class Panel {

    int panelX;
    int panelY;
    int width;
    int height;
    Panel parent;
    boolean dirty = false;
    boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getPanelX() {
        return panelX;
    }

    public void setPanelX(int panelX) {
        this.panelX = panelX;
        dirty = true;
    }

    public int getPanelY() {
        return panelY;
    }

    public void setPanelY(int panelY) {
        this.panelY = panelY;
        dirty = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        dirty = true;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        dirty = true;
    }

    protected abstract void draw(TextGraphics tg);

    public void drawIfDirty(TextGraphics tg) {
        if (dirty) draw(tg);
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public Panel getParent() {
        return parent;
    }

    public void setParent(Panel parent) {
        this.parent = parent;
    }

    public void setPosition(int x, int y) {
        this.panelX = x;
        this.panelY = y;
        dirty = true;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        dirty = true;
    }

    // Screen position combined from all parent positions.
    public Point getCombinedPosition() {
        Panel head = this;
        int xx = 0, yy = 0;
        while (head != null) {
            xx += head.getPanelX();
            yy += head.getPanelY();
            head = head.getParent();
        }

        return new Point(xx, yy);
    }
}
