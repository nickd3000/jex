package com.physmo;

import com.physmo.panels.FilePanel;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class TestFilePanel {
    @Test
    @Ignore
    public void t1() throws IOException {
        Harness harness = new Harness();
        harness.start();

        FilePanel filePanel = new FilePanel();
        filePanel.setSize(30, 13);
        filePanel.setPosition(5, 5);

        boolean running = true;

        while (running) {
            try {
                filePanel.drawChildren(harness.getTg(), true);
                harness.getScreen().refresh();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
