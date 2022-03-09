package com.physmo;

import java.util.ArrayList;
import java.util.List;

public class LineSplitter {

    int usableWidth;

    public LineSplitter(int usableWidth, int mode) {
        this.usableWidth=usableWidth;
    }




    public int calculateHeightOfLine(String text) {
        int height = text.length()/usableWidth;
        if (text.length()%usableWidth>0) height++;
        if (height<1) height=1;
        return height;
    }

    // should we return array of line starts and lengths instead of individual strings?
    // returns a list of pairs:
    //   [i]   start of section
    //   [i+1] length of section
    public int[] split(String str) {

        return split_dumb(str);
    }

    public int[] split_dumb(String str) {
        List<Integer> list = new ArrayList<>();

        int chunkStart=0;
        int strLength = str.length();

        while (chunkStart<strLength) {
            if (chunkStart + usableWidth < strLength) {
                list.add(chunkStart);
                list.add(usableWidth);
                chunkStart += usableWidth;
            } else {
                int remainder = strLength - chunkStart;
                list.add(chunkStart);
                list.add(remainder);
                chunkStart += remainder;
            }
        }

        return list.stream().mapToInt(i -> i).toArray();
    }
}
