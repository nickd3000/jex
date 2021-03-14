package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Point;

import java.util.ArrayList;
import java.util.List;

// base class for all windows and elements
public abstract class Panel {

    List<Panel> children = new ArrayList<>();

    // todo change to points
    Point position = new Point(0, 0);
    Point size = new Point(0, 0);

//    int panelX;
//    int panelY;
//    int width;
//    int height;

    Panel parent;
    boolean dirty = false;
    boolean visible = true;

    public void addChild(Panel child) {
        if (children.contains(child)) throw new ArrayStoreException("Child already exists");
        children.add(child);
        child.setParent(this);
    }

    public void setParent(Panel parent) {
        this.parent = parent;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void drawIfDirty(TextGraphics tg) {
        if (dirty) draw(tg);
        dirty = false;
    }

//    public int getWidth() {
//        return width;
//    }
//
//    public void setWidth(int width) {
//        this.width = width;
//        dirty = true;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//        dirty = true;
//    }

    protected abstract void draw(TextGraphics tg);

    public boolean isDirty() {
        return dirty;
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
        dirty = true;
    }

    public void setPosition(Point p) {
        this.position = new Point(p);
        dirty = true;
    }

    public Point getSize() {
        return size;
    }

    public void setSize(int w, int h) {
        this.size = new Point(w, h);
        dirty = true;
    }

    public void setSize(Point p) {
        this.size = new Point(p);
        dirty = true;
    }

    // Screen position combined from all parent positions.
    public Point getCombinedPosition() {
        Panel head = this;
        int xx = 0, yy = 0;
        while (head != null) {
            xx += head.getPosition().x;
            yy += head.getPosition().y;
            head = head.getParent();
        }

        return new Point(xx, yy);
    }

    public Panel getParent() {
        return parent;
    }

    public Point getPosition() {
        return position;
    }

//    public int getPanelX() {
//        return panelX;
//    }
//
//    public int getPanelY() {
//        return panelY;
//    }


//    public void setPanelX(int panelX) {
//        this.panelX = panelX;
//        dirty = true;
//    }
//
//    public void setPanelY(int panelY) {
//        this.panelY = panelY;
//        dirty = true;
//    }
}
