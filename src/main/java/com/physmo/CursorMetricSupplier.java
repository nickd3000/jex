package com.physmo;

public interface CursorMetricSupplier {
    int getLineLength(int lineNumber);
    int getTotalLines();
    int getStartOfLineIndex(int lineNumber);

    int getMajorLineNumber(int y);
}
