package com.physmo.document;

import com.physmo.buffers.TextBuffer;

public class DocumentContainer {
    // document text and state
    int id;
    String path;
    TextBuffer textBuffer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TextBuffer getTextBuffer() {
        return textBuffer;
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }

}
