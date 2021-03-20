package com.physmo.panels;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private String name;
    private List<MenuItem> children;

    public MenuItem(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }


}
