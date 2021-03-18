package com.physmo.panels;

public class FocusTraverser {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int BACK = 4;
    public static final int FORWARD = 5;
    Panel[] targets = new Panel[FORWARD + 1];

    public void setTarget(int direction, Panel panel) {
        targets[direction] = panel;
    }

    public Panel getTarget(int direction) {
        return targets[direction];
    }
}
