package com.physmo;

import com.physmo.panels.Viewport;

import java.util.HashMap;
import java.util.Map;

public class ViewPortRepo {

    int numberFountain = 0;
    Map<Integer, Viewport> viewportMap = new HashMap<>();

    public Viewport getViewportById(int id) {
        return viewportMap.get(id);
    }

    public int createViewport(MainApp mainApp) {
        Viewport vp = new Viewport(mainApp);
        vp.setId(numberFountain++);
        viewportMap.put(vp.getId(), vp);
        return vp.getId();
    }

}
