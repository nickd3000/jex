package com.physmo;

import com.physmo.command.Commands;
import com.physmo.panels.ListElement;
import com.physmo.panels.ListPanel;
import com.physmo.panels.MenuBar;

// Responsible for populating menu bar and handling actions.
public class MenuBarManager {
    MainApp mainApp;

    public MenuBarManager(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void initMenuBar(MenuBar menuBar) {

        menuBar.addMenuHeader("File");
        menuBar.addMenuHeader("Edit");
        menuBar.addMenuHeader("View");
        menuBar.addMenuHeader("Options");

        ListPanel fileSubMenu = new ListPanel();
        fileSubMenu.getList().add(new ListElement("New", Commands.FILE_NEW));
        fileSubMenu.getList().add(new ListElement("Open...", Commands.FILE_OPEN));
        fileSubMenu.getList().add(new ListElement("Save", "FILE_SAVE"));
        fileSubMenu.getList().add(new ListElement("Save As", "FILE_SAVE_AS"));
        fileSubMenu.getList().add(new ListElement("test", Commands.FILE_TEST));
        fileSubMenu.getList().add(new ListElement("Exit", Commands.FILE_EXIT));
        fileSubMenu.setVisible(true);
        fileSubMenu.sizeToContent(10);
        fileSubMenu.setPosition(menuBar.getTopLevelMenuItemXPosition(0), 1);
        fileSubMenu.doLayout();
        fileSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel editSubMenu = new ListPanel();
        editSubMenu.getList().add(new ListElement("Cut", ""));
        editSubMenu.getList().add(new ListElement("Copy", ""));
        editSubMenu.getList().add(new ListElement("Paste", ""));
        editSubMenu.getList().add(new ListElement("Clear", ""));
        editSubMenu.setVisible(false);
        editSubMenu.sizeToContent(10);
        editSubMenu.setPosition(menuBar.getTopLevelMenuItemXPosition(1), 1);
        editSubMenu.doLayout();
        editSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel searchSubMenu = new ListPanel();
        searchSubMenu.getList().add(new ListElement("Wrap off", ""));
        searchSubMenu.getList().add(new ListElement("Word Wrap", ""));
        searchSubMenu.getList().add(new ListElement("Length Wrap", ""));
        searchSubMenu.setVisible(false);
        searchSubMenu.sizeToContent(10);
        searchSubMenu.setPosition(menuBar.getTopLevelMenuItemXPosition(2), 1);
        searchSubMenu.doLayout();
        searchSubMenu.addSelectionHandler(this::selectionHandler);

        ListPanel optionsSubMenu = new ListPanel();
        optionsSubMenu.getList().add(new ListElement("test", ""));
        optionsSubMenu.setVisible(false);
        optionsSubMenu.sizeToContent(10);
        optionsSubMenu.setPosition(menuBar.getTopLevelMenuItemXPosition(3), 1);
        optionsSubMenu.doLayout();
        optionsSubMenu.addSelectionHandler(this::selectionHandler);

        menuBar.addSubMenuList(0, fileSubMenu);
        menuBar.addSubMenuList(1, editSubMenu);
        menuBar.addSubMenuList(2, searchSubMenu);
        menuBar.addSubMenuList(3, optionsSubMenu);
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
        if (action.equals(Commands.FILE_TEST)) {
            mainApp.getCommandQueue().push(Commands.FILE_TEST, null);
        }
    }
}
