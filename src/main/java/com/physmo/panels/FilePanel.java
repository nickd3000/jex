package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.physmo.Utilities;

import java.io.File;

public class FilePanel extends Panel {
    Button okButton;
    Button cancelButton;

    public FilePanel() {
        okButton = new Button();
        okButton.text = "<ok>";
        this.addChild(okButton);

        cancelButton = new Button();
        cancelButton.text = "<cancel>";
        this.addChild(cancelButton);

        doLayout();
    }

    public void doLayout() {
        okButton.setPosition(2, 2);
        cancelButton.setPosition(12, 2);
    }

    @Override
    public void draw(TextGraphics tg) {
        Utilities.drawBox(tg, position.x, position.y, size.x, size.y, '*');

        Utilities.drawBox(tg, 0, 0, 4, 4, '*');
    }

    public void getFileList(String path) {
        File f = new File("/");
        File[] files = f.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }

    }
}
