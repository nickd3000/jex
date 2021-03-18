package com.physmo;

import com.googlecode.lanterna.input.KeyStroke;
import com.physmo.panels.FilePanel;
import com.physmo.panels.ListElement;
import com.physmo.panels.ListPanel;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestListPanel {
    @Test
    @Ignore
    public void t1() throws IOException {
        Harness harness = new Harness();
        harness.start();

        ListPanel listPanel = new ListPanel();
        listPanel.setSize(30 / 3, 4);
        listPanel.setPosition(5, 5);
        listPanel.doLayout();

        List<ListElement> list = new ArrayList<>();
        list.add(new ListElement("nick", null));
        list.add(new ListElement("nick1", null));
        list.add(new ListElement("nick2", null));
        list.add(new ListElement("nick3_67890abcd", null));
        list.add(new ListElement("nick4", null));
        list.add(new ListElement("nick5", null));
        list.add(new ListElement("nick6", null));
        list.add(new ListElement("nick7", null));

        listPanel.setList(list);
        listPanel.addSelectionHandler((index, object) -> {
            System.out.println("index:" + index);
        });

        boolean running = true;

        while (running) {
            try {
                listPanel.drawChildren(harness.getTg(), true);
                harness.getScreen().refresh();
                Thread.sleep(100);

                KeyStroke keyStroke = harness.getTerminal().pollInput();
                while (keyStroke != null) {
                    listPanel.processKeystroke(keyStroke);
                    keyStroke = harness.getTerminal().pollInput();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetFileList() {
        FilePanel filePanel = new FilePanel();
        File[] fileList = filePanel.getFileList("/");

        for (File file : fileList) {
            System.out.println(file.getName());
        }

    }

}
