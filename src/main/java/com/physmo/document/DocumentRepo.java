package com.physmo.document;

import java.util.HashMap;
import java.util.Map;

public class DocumentRepo {
    int numberFountain = 0;
    Map<Integer, DocumentContainer> documentMap = new HashMap<>();

    public DocumentContainer getDocumentById(int id) {
        return documentMap.get(id);
    }

    public int createEmptyDocument() {
        DocumentContainer dc = new DocumentContainer();
        dc.id = numberFountain++;
        documentMap.put(dc.id, dc);
        return dc.id;
    }

}
