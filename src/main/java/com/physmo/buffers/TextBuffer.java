package com.physmo.buffers;

public abstract class TextBuffer {

    public abstract void setInitialText(String text);

    public abstract String getLine(int lineNumber);

    public abstract String getCharacter(int position);

    public abstract void insert(int position, String text);

    public abstract int getLineCount();

    public abstract int getStartOfLineIndex(int line);

    public abstract void deleteCharacter(int position);

    public abstract boolean isEmpty();
}
