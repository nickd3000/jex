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
    String currentPath = "/Users/nick";

    Panel focusedPanel;

    Callback loadFileCallback;
    Callback closeDialogCallback;

    public void addLoadFileCallback(Callback loadFileCallback) {
        this.loadFileCallback =  loadFileCallback;
    }
    public void addCloseDialogCallback(Callback closeDialogCallback) {
        this.closeDialogCallback =  closeDialogCallback;
    }


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
        okButton.addCallback(o -> {
            System.out.println("ok button pressed");
        });

        cancelButton = new Button();
        cancelButton.text = "<cancel>";
        this.addChild(cancelButton);

        listPanel = new ListPanel();

        this.addChild(listPanel);

        focusedPanel = listPanel;
        focusedPanel.setFocus(true);

        updateFileListPanelOnPathChange();
        setFocusTargets();
        doLayout();

        listPanel.addSelectionHandler((index, object) -> {
            System.out.println("Selected file: " + index);
            // we ned to tell the main app to load this file then close the dialog
            ListElement le = (ListElement) object;
            File file = ((File)le.object);
            String fullPath = file.getPath();
            //fullPath+=file.getName();
            loadFileCallback.callback(fullPath);
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
            String displayString = getDisplayStringForFile(file);
            ListElement newListElement = new ListElement(displayString, file);
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
        if (!isVisible()) return;

        tg.setForegroundColor(colorMap.get("BG"));
        tg.setBackgroundColor(colorMap.get("FG"));

        Utilities.drawBox(tg, position.x, position.y, size.x, size.y, ' ');

        //Utilities.drawBox(tg, 0, 0, 4, 4, '*');
    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {
        super.processKeystroke(keyStroke);

        boolean consumed = false;

        if (focusedPanel == pathPanel) {
            pathPanel.processKeystroke(keyStroke);
        }

        if (keyStroke.getKeyType() == KeyType.Tab) {
            consumed = true;
            focusedPanel.setFocus(false);
            focusedPanel = focusedPanel.getFocusTraverser().getTarget(FocusTraverser.FORWARD);
            focusedPanel.setFocus(true);
        }

        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            Panel target = focusedPanel.getFocusTraverser().getTarget(FocusTraverser.RIGHT);
            if (target != null) {
                focusedPanel.setFocus(false);
                focusedPanel = target;
                focusedPanel.setFocus(true);
            }
        }

        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            Panel target = focusedPanel.getFocusTraverser().getTarget(FocusTraverser.LEFT);
            if (target != null) {
                focusedPanel.setFocus(false);
                focusedPanel = target;
                focusedPanel.setFocus(true);
            }
        }


        if (focusedPanel == listPanel) {
            listPanel.processKeystroke(keyStroke);
        }

        if (focusedPanel==okButton || focusedPanel==cancelButton) {
            focusedPanel.processKeystroke(keyStroke);
        }

        return consumed;
    }

    public static String getDisplayStringForFile(File file) {
        long bytes = file.length();
        file.getName();
        String fileSize = (prettyFileSize(bytes)+"     ").substring(0,8);
        String result = fileSize+" "+file.getName();
        return result;
    }

    public static String prettyFileSize(long bytes) {
        return Utilities.humanReadableByteCountBin(bytes);
    }
}
