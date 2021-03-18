package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.physmo.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// base class for all windows and elements
public abstract class Panel {

    List<Panel> children = new ArrayList<>();

    // todo change to points
    Point position = new Point(0, 0);
    Point size = new Point(0, 0);
    Panel parent;
    boolean dirty = true;
    boolean visible = true;
    boolean focus = false;
    FocusTraverser focusTraverser = new FocusTraverser();

    public FocusTraverser getFocusTraverser() {
        return focusTraverser;
    }

    public boolean hasFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        dirty = true;
        this.focus = focus;
    }

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

    // Breadth first drawing of children.
    public void drawChildren(TextGraphics tg, boolean forceDraw) {
        Queue<Panel> queue = new ArrayDeque<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            Panel p = queue.poll();
            p.drawIfDirty(tg, forceDraw);

            for (Panel child : p.getChildren()) {
                queue.add(child);
            }
        }
    }

    public void drawIfDirty(TextGraphics tg, boolean forceDraw) {
        if (dirty || forceDraw) draw(tg);
        dirty = false;
    }


    protected abstract void draw(TextGraphics tg);

    public List<Panel> getChildren() {
        return children;
    }

    protected boolean processKeystroke(KeyStroke keyStroke) {
        return false;
    }

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

}