package com.physmo;

import com.physmo.panels.EditorPanel;
import org.junit.Test;

public class TestEditorPanel {

    @Test
    public void t1() {
        EditorPanel editorPanel = new EditorPanel(null);

        System.out.println(editorPanel.calculateHeightofLine("aaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaaaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaaaaaa", 3));
        System.out.println(editorPanel.calculateHeightofLine("aaaaaaaaaa", 3));
    }

}
