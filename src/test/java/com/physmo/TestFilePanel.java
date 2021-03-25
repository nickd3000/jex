package com.physmo;

import com.googlecode.lanterna.input.KeyStroke;
import com.physmo.panels.FilePanel;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestFilePanel {
    @Test
    @Ignore
    public void t1() throws IOException {
        Harness harness = new Harness();
        harness.start();

        FilePanel filePanel = new FilePanel();
        filePanel.setSize(60, 16);
        filePanel.setPosition(5, 5);

        filePanel.addLoadFileCallback(o -> {
            System.out.println("file: " + (String) o);
        });

        boolean running = true;

        while (running) {
            try {
                filePanel.drawChildren(harness.getTg(), true);
                harness.getScreen().refresh();
                Thread.sleep(100);

                KeyStroke keyStroke = harness.getTerminal().pollInput();
                while (keyStroke != null) {
                    filePanel.processKeystroke(keyStroke);
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

    @Test
    public void testPrettyFileSize() {

        System.out.println(FilePanel.prettyFileSize(100));
        System.out.println(FilePanel.prettyFileSize(2000));
        System.out.println(FilePanel.prettyFileSize(2000000));


    }
}
