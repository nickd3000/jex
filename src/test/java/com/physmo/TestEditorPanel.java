package com.physmo;

import com.physmo.editorpanel.EditorPanel;
import org.junit.Assert;
import org.junit.Test;

public class TestEditorPanel {

    @Test
    public void testConvertTabCharacters() {
        EditorPanel editorPanel = new EditorPanel(null);
        String str = editorPanel.convertTabCharacters("...\t...\t", 8);
        System.out.println(str);
    }

    @Test
    public void testBuildMarginString() {
        EditorPanel editorPanel = new EditorPanel(null);

        Assert.assertEquals("  0",editorPanel.buildMarginString(4, 0));
        Assert.assertEquals("  1",editorPanel.buildMarginString(4, 1));
        Assert.assertEquals("  5",editorPanel.buildMarginString(4, 5));
        Assert.assertEquals("100",editorPanel.buildMarginString(4, 100));
        Assert.assertEquals("   ",editorPanel.buildMarginString(4, -1));


    }


    @Test
    public void testCalculateMarginSizeForTotalLines() {
        EditorPanel editorPanel = new EditorPanel(null);

        Assert.assertEquals(1,editorPanel.calculateMarginSizeForTotalLines(1));
        Assert.assertEquals(1,editorPanel.calculateMarginSizeForTotalLines(2));
        Assert.assertEquals(1,editorPanel.calculateMarginSizeForTotalLines(9));
        Assert.assertEquals(2,editorPanel.calculateMarginSizeForTotalLines(10));
        Assert.assertEquals(2,editorPanel.calculateMarginSizeForTotalLines(11));
        Assert.assertEquals(2,editorPanel.calculateMarginSizeForTotalLines(99));
        Assert.assertEquals(3,editorPanel.calculateMarginSizeForTotalLines(100));
        Assert.assertEquals(3,editorPanel.calculateMarginSizeForTotalLines(101));
        Assert.assertEquals(3,editorPanel.calculateMarginSizeForTotalLines(999));
        Assert.assertEquals(4,editorPanel.calculateMarginSizeForTotalLines(1000));
        Assert.assertEquals(4,editorPanel.calculateMarginSizeForTotalLines(1001));



    }
}
