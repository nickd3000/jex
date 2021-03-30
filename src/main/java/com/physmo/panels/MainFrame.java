package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
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
    }

    public void doLayout() {
        // Menu bar
        menuBar.setPosition(0, 0);
        menuBar.setSize(size.x, size.y);

        // Info Bar
        infoBar.setPosition(0, size.y - 1);
        infoBar.setSize(size.x, 1);

        mainApp.getActiveViewport().ifPresent(vp -> {
            vp.setPosition(0, 1);
            vp.setSize(size.x, size.y - 2);
            vp.doLayout();
        });

    }

    @Override
    public void draw(TextGraphics tg) {

        mainApp.getActiveViewport().ifPresent(vp -> {
            vp.draw(tg);
        });

        menuBar.draw(tg);
        infoBar.draw(tg);
    }

    public void addViewport(Viewport vp) {
        this.addChild(vp);
        doLayout();
        vp.setFocus(true);
    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {
        //return super.processKeystroke(keyStroke);

        if (keyStroke.getKeyType() == KeyType.Escape) {
            mainApp.getActiveViewport().ifPresentOrElse(vp -> {
                if (vp.hasFocus()) {
                    menuBar.setFocus(true);
                    vp.setFocus(false);
                } else {
                    menuBar.setFocus(false);
                    vp.setFocus(true);
                }
            }, () -> {
                menuBar.setFocus(true);

            });

        }

        if (menuBar.hasFocus()) {
            menuBar.processKeystroke(keyStroke);
        }

        mainApp.getActiveViewport().ifPresent(vp -> {
            if (vp.hasFocus()) {
                vp.processKeystroke(keyStroke);
            }
        });


        return false;
    }

    public void hideMenuBar() {
        menuBar.setFocus(false);
        mainApp.getActiveViewport().ifPresent(vp -> {
            vp.setFocus(true);
        });
    }
}
