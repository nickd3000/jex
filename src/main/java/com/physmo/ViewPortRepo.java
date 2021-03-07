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

    public int createViewport() {
        Viewport vp = new Viewport();
        vp.setId(numberFountain++);
        viewportMap.put(vp.getId(), vp);
        return vp.getId();
    }

    /*
        int numberFountain = 0;
    Map<Integer, DocumentContainer> documentMap;

    public DocumentContainer getDocumentById(int id) {
        return documentMap.get(id);
    }

    public int createEmptyDocument() {
        DocumentContainer dc = new DocumentContainer();
        dc.id = numberFountain++;
        documentMap.put(dc.id, dc);
        return dc.id;
    }
     */
}
