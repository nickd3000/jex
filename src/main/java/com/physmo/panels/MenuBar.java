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

//        addMenuHeader("File");
//        addMenuHeader("Edit");
//        addMenuHeader("Search");
//        addMenuHeader("Options");

        subMenuList = new ArrayList<>();
    }

    public void addMenuHeader(String name) {
        menuList.getChildren().add(new MenuItem(name));
    }

    public void addSubMenuList(int index, ListPanel subMenu) {
        subMenuList.add(index, subMenu);
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
