package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.MainApp;
import com.physmo.Point;

public class MainFrame extends Panel {

    MainApp mainApp;
    InfoBar infoBar;
    MenuBar menuBar;

    public MainFrame(MainApp mainApp, int width, int height) {
        this.mainApp = mainApp;

        this.setSize(new Point(width, height));

        infoBar = new InfoBar(mainApp);
        addChild(infoBar);

        menuBar = new MenuBar(mainApp);
        addChild(menuBar);

        doLayout();
//        infoBar.setPosition(0, height - 1);
//        infoBar.setSize(width, 1);


    }

    public void doLayout() {
        // Menu bar
        menuBar.setPosition(0, 0);
        menuBar.setSize(size.x, size.y);

        // Info Bar
        infoBar.setPosition(0, size.y - 1);
        infoBar.setSize(size.x, 1);

        Viewport vp = mainApp.getActiveViewport();
        if (vp != null) {
            vp.setPosition(0, 1);
            vp.setSize(size.x, size.y - 2);
            vp.doLayout();
        }
    }

    @Override
    protected void draw(TextGraphics tg) {

    }

    public void addViewport(Viewport vp) {
        this.addChild(vp);
        doLayout();
    }
}
