package com.physmo.panels;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.physmo.Point;
import com.physmo.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListPanel extends Panel {

    String heading = "files";
    List<ListElement> list;
    int scrollOffset = 0;
    int selectedIndex = 0;
    Map<String, TextColor> colorMap;
    boolean showBorders = true;
    boolean highlightFullWidth = true;
    int visibleRows = 0;
    int visibleColumns = 0;
    ListPanelCallback selectionHandler = null;
    boolean wrapSelection = true;

    public ListPanel() {
        list = new ArrayList<>();
        colorMap = new HashMap<>();
        colorMap.put("FG", TextColor.ANSI.WHITE);
        colorMap.put("BG", TextColor.ANSI.RED);

        doLayout();
    }

    public void doLayout() {
        if (showBorders) visibleRows = size.y - 2;
        else visibleRows = size.y;
        if (showBorders) visibleColumns = size.x - 2;
        else visibleColumns = size.x;
    }

    public List<ListElement> getList() {
        return list;
    }

    public void setList(List<ListElement> list) {
        this.list = list;
    }

    @Override
    protected void draw(TextGraphics tg) {
        if (!visible) return;

        tg.setForegroundColor(colorMap.get("FG"));
        tg.setBackgroundColor(colorMap.get("BG"));

        Point combinedPosition = getCombinedPosition();
        if (showBorders) {
            Utilities.drawBox(tg, combinedPosition, size, ' ');
            if (heading.length() > 0) {
                tg.putString(combinedPosition.x + 2, combinedPosition.y, heading);
            }
        } else {
            Utilities.fillRectangle(tg, combinedPosition, size, ' ');
        }


        drawList(tg);
    }

    public void drawList(TextGraphics tg) {
        int listSize = list.size();
        Point combinedPosition = getCombinedPosition();
        int xShift = 0, yShift = 0;
        if (showBorders) xShift = yShift = 1;

        for (int i = 0; i < visibleRows; i++) {
            if (i + scrollOffset >= listSize) continue;
            ListElement listElement = list.get(i + scrollOffset);
            String elementText = listElement.name;
            if (highlightFullWidth) elementText += "                   ";
            if (elementText.length() > visibleColumns) elementText = elementText.substring(0, visibleColumns);

            boolean rowIsSelected = false;
            if (i + scrollOffset == selectedIndex) rowIsSelected = true;
            if (!hasFocus()) rowIsSelected = false;

            // Set colors, flip them if this is the selected list item.
            if (rowIsSelected) {
                tg.setForegroundColor(colorMap.get("BG"));
                tg.setBackgroundColor(colorMap.get("FG"));
            } else {
                tg.setForegroundColor(colorMap.get("FG"));
                tg.setBackgroundColor(colorMap.get("BG"));
            }

            tg.putString(combinedPosition.x + xShift, combinedPosition.y + yShift + i, elementText);
        }

    }

    public void prevItem() {
        selectedIndex--;

        if (wrapSelection) {
            if (selectedIndex < 0) selectedIndex = list.size() - 1;
        }
        else {
            if (selectedIndex < 0) selectedIndex = 0;
        }
    }
    public void nextItem() {
        selectedIndex++;

        if (wrapSelection) {
            if (selectedIndex > list.size() - 1) selectedIndex = 0;
        }
        else {
            if (selectedIndex >= list.size() - 1) selectedIndex = list.size() - 1;
        }
    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {

        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            prevItem();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            nextItem();
        }

        if (keyStroke.getKeyType() == KeyType.Enter) {
            if (selectedIndex<list.size()) {
                ListElement selectedObject = list.get(selectedIndex);
                selectionHandler.call(selectedIndex, selectedObject);
            }
        }

        while (selectedIndex < scrollOffset) {
            scrollOffset--;
        }
        while (selectedIndex >= scrollOffset + (visibleRows)) {
            scrollOffset++;
        }
        return false;
    }

    public void addSelectionHandler(ListPanelCallback f) {
        selectionHandler = f;
    }

    public void sizeToContent(int minWidth) {
        size.x = minWidth;
        size.y = list.size();
        if (showBorders) {
            size.x += 2;
            size.y += 2;
        }
    }

    public void setSelectedIndex(int i) {
        selectedIndex=i;
    }
}
