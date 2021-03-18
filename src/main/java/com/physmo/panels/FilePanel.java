package com.physmo.panels;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.physmo.Utilities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilePanel extends Panel {

    Map<String, TextColor> colorMap;
    TextEntryPanel pathPanel;
    Button okButton;
    Button cancelButton;
    ListPanel listPanel;
    String currentPath = "/tmp";

    Panel focusPanel;

    List<Panel> focusList;

    public FilePanel() {
        colorMap = new HashMap<>();
        colorMap.put("FG", TextColor.ANSI.RED);
        colorMap.put("BG", TextColor.ANSI.WHITE);

        pathPanel = new TextEntryPanel();
        pathPanel.text = "/tmp";
        this.addChild(pathPanel);

        okButton = new Button();
        okButton.text = "<ok>";
        this.addChild(okButton);

        cancelButton = new Button();
        cancelButton.text = "<cancel>";
        this.addChild(cancelButton);

        listPanel = new ListPanel();

        this.addChild(listPanel);

        focusPanel = listPanel;

        updateFileListPanelOnPathChange();
        setFocusTargets();
        doLayout();

        listPanel.addSelectionHandler((index, object) -> {
            System.out.println("Selected file: " + index);
        });

    }

    public void setFocusTargets() {
        pathPanel.getFocusTraverser().setTarget(FocusTraverser.FORWARD, listPanel);
        pathPanel.getFocusTraverser().setTarget(FocusTraverser.BACK, cancelButton);

        listPanel.getFocusTraverser().setTarget(FocusTraverser.FORWARD, okButton);
        listPanel.getFocusTraverser().setTarget(FocusTraverser.BACK, pathPanel);

        okButton.getFocusTraverser().setTarget(FocusTraverser.FORWARD, cancelButton);
        okButton.getFocusTraverser().setTarget(FocusTraverser.BACK, listPanel);
        okButton.getFocusTraverser().setTarget(FocusTraverser.LEFT, cancelButton);
        okButton.getFocusTraverser().setTarget(FocusTraverser.RIGHT, cancelButton);

        cancelButton.getFocusTraverser().setTarget(FocusTraverser.FORWARD, pathPanel);
        cancelButton.getFocusTraverser().setTarget(FocusTraverser.BACK, okButton);
        cancelButton.getFocusTraverser().setTarget(FocusTraverser.LEFT, okButton);
        cancelButton.getFocusTraverser().setTarget(FocusTraverser.RIGHT, okButton);
    }

    public void doLayout() {
        pathPanel.setPosition(2, 2);
        okButton.setPosition(2, size.y - 3);
        cancelButton.setPosition(12, size.y - 3);
        listPanel.setPosition(2, 4);
        listPanel.setSize(size.x - 8, size.y - 8);
        listPanel.doLayout();
    }

    public void updateFileListPanelOnPathChange() {
        File[] fileList = getFileList(currentPath);
        List<ListElement> listPanelList = listPanel.getList();

        listPanelList.clear();

        for (File file : fileList) {
            String fileName = file.getName();
            ListElement newListElement = new ListElement(fileName, file);
            listPanelList.add(newListElement);
        }

    }

    public File[] getFileList(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        return files;
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        doLayout();
    }

    @Override
    public void draw(TextGraphics tg) {
        tg.setForegroundColor(colorMap.get("BG"));
        tg.setBackgroundColor(colorMap.get("FG"));

        Utilities.drawBox(tg, position.x, position.y, size.x, size.y, ' ');

        //Utilities.drawBox(tg, 0, 0, 4, 4, '*');
    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {
        super.processKeystroke(keyStroke);

        boolean consumed = false;

        if (focusPanel == pathPanel) {
            pathPanel.processKeystroke(keyStroke);
        }

        if (keyStroke.getKeyType() == KeyType.Tab) {
            consumed = true;
            focusPanel.setFocus(false);
            focusPanel = focusPanel.getFocusTraverser().getTarget(FocusTraverser.FORWARD);
            focusPanel.setFocus(true);
        }

        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            Panel target = focusPanel.getFocusTraverser().getTarget(FocusTraverser.RIGHT);
            if (target != null) {
                focusPanel.setFocus(false);
                focusPanel = target;
                focusPanel.setFocus(true);
            }
        }

        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            Panel target = focusPanel.getFocusTraverser().getTarget(FocusTraverser.LEFT);
            if (target != null) {
                focusPanel.setFocus(false);
                focusPanel = target;
                focusPanel.setFocus(true);
            }
        }


        if (focusPanel == listPanel) {
            listPanel.processKeystroke(keyStroke);
        }

        return consumed;
    }
}
