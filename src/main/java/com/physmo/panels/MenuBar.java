package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.physmo.ColorRepo;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.Utilities;
import com.physmo.command.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuBar extends Panel {

    MainApp mainApp;
    MenuItem menuList;
    int selectedIndex = 0;
    boolean dropdownVisible = false;
    List<Panel> subMenuList;

    public MenuBar(MainApp mainApp) {
        this.mainApp = mainApp;
        menuList = new MenuItem("root");
        for (String s : Arrays.asList("File", "Edit", "Search", "Options")) {
            menuList.getChildren().add(new MenuItem(s));
        }
        subMenuList = new ArrayList<>();


        ListPanel fileSubMenu = new ListPanel();
        fileSubMenu.getList().add(new ListElement("New", Commands.FILE_NEW));
        fileSubMenu.getList().add(new ListElement("Open...", Commands.FILE_OPEN));
        fileSubMenu.getList().add(new ListElement("Save", "FILE_SAVE"));
        fileSubMenu.getList().add(new ListElement("Save As", "FILE_SAVE_AS"));
        fileSubMenu.getList().add(new ListElement("Exit", Commands.FILE_EXIT));
        fileSubMenu.setVisible(true);
        fileSubMenu.sizeToContent(10);
        fileSubMenu.setPosition(getTopLevelMenuItemXPosition(0), 1);
        fileSubMenu.doLayout();
        fileSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel editSubMenu = new ListPanel();
        editSubMenu.getList().add(new ListElement("Cut", ""));
        editSubMenu.getList().add(new ListElement("Copy", ""));
        editSubMenu.getList().add(new ListElement("Paste", ""));
        editSubMenu.getList().add(new ListElement("Clear", ""));
        editSubMenu.setVisible(false);
        editSubMenu.sizeToContent(10);
        editSubMenu.setPosition(getTopLevelMenuItemXPosition(1), 1);
        editSubMenu.doLayout();
        editSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel searchSubMenu = new ListPanel();
        searchSubMenu.getList().add(new ListElement("test", ""));
        searchSubMenu.setVisible(false);
        searchSubMenu.sizeToContent(10);
        searchSubMenu.setPosition(getTopLevelMenuItemXPosition(2), 1);
        searchSubMenu.doLayout();
        searchSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel optionsSubMenu = new ListPanel();
        optionsSubMenu.getList().add(new ListElement("test", ""));
        optionsSubMenu.setVisible(false);
        optionsSubMenu.sizeToContent(10);
        optionsSubMenu.setPosition(getTopLevelMenuItemXPosition(3), 1);
        optionsSubMenu.doLayout();
        optionsSubMenu.addSelectionHandler(this::selectionHandler);

        subMenuList.add(0, fileSubMenu);
        subMenuList.add(1, editSubMenu);
        subMenuList.add(2, searchSubMenu);
        subMenuList.add(3, optionsSubMenu);
    }

    public void selectionHandler(int index, Object object) {
        ListElement listElement = (ListElement) object;
        String action = (String) listElement.object;

        if (action.equals("FILE_EXIT")) {
            mainApp.getCommandQueue().push(Commands.FILE_EXIT, null);
        }
        if (action.equals(Commands.FILE_OPEN)) {
            mainApp.getCommandQueue().push(Commands.FILE_OPEN, null);
        }
        if (action.equals(Commands.FILE_NEW)) {
            mainApp.getCommandQueue().push(Commands.FILE_NEW, null);
        }
    }

    public int getTopLevelMenuItemXPosition(int index) {
        int xPos = 0;
        xPos += 3;
        int padding = 3;
        for (int i = 0; i < index; i++) {
            xPos += menuList.getChildren().get(i).getName().length();
            xPos += padding;
        }
        return xPos;
    }

    @Override
    public void setFocus(boolean focus) {
        super.setFocus(focus);
        if (!this.hasFocus()) {
            setDropdownVisible(false);
        }
    }

    public void setDropdownVisible(boolean visible) {
        dropdownVisible = visible;
    }

    @Override
    protected void draw(TextGraphics tg) {
        mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.MENU_BAR);

        Utilities.fillRectangle(tg, 0, 0, size.x, 1, ' ');

        int index = 0;
        for (MenuItem child : menuList.getChildren()) {
            int xPos = getTopLevelMenuItemXPosition(index);

            if (selectedIndex == index && this.hasFocus()) {
                mainApp.getColorRepo().setThemeElementColorInverted(tg, ColorRepo.MENU_BAR);
            } else {
                mainApp.getColorRepo().setThemeElementColor(tg, ColorRepo.MENU_BAR);
            }
            tg.putString(xPos, 0, " " + child.getName() + " ");
            index++;
        }

        if (dropdownVisible) {
            for (Panel panel : subMenuList) {
                panel.draw(tg);
            }
        }

    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {
        //return super.processKeystroke(keyStroke);

        KeyType keyType = keyStroke.getKeyType();

        if (keyType == KeyType.ArrowRight) {
            selectedIndex++;
            if (selectedIndex >= menuList.getChildren().size()) selectedIndex = 0;
            showSelectedDropdown();
            return true;
        }
        if (keyType == KeyType.ArrowLeft) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = menuList.getChildren().size() - 1;
            showSelectedDropdown();
            return true;
        }

        if (keyType == KeyType.Enter && !dropdownVisible) {
            setDropdownVisible(true);
            showSelectedDropdown();
            return true;
        }

        if (keyType == KeyType.ArrowDown && !dropdownVisible) {
            setDropdownVisible(true);
            showSelectedDropdown();
            return true;
        }

        if (keyType == KeyType.ArrowUp && !dropdownVisible) {
            setDropdownVisible(true);
            showSelectedDropdown();
            return true;
        }

        if (dropdownVisible) {
            if (keyType == KeyType.ArrowUp ||
                    keyType == KeyType.ArrowDown ||
                    keyType == KeyType.Enter) {
                subMenuList.get(selectedIndex).processKeystroke(keyStroke);
            }
        }

        return false;
    }

    public void showSelectedDropdown() {
        if (dropdownVisible) {
            for (Panel panel : subMenuList) {
                panel.setFocus(false);
                panel.setVisible(false);
            }
            Panel selectedMenu = subMenuList.get(selectedIndex);
            selectedMenu.setFocus(true);
            selectedMenu.setVisible(true);
            ((ListPanel) selectedMenu).setSelectedIndex(0);
        }
    }
}
