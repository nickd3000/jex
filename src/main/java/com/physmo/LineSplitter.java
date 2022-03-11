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
    //   [i+1] length of section (in raw chars)
    //   [i+2] length of section (expanded tabs)
    public int[] split(String str) {

        return split_dumb_tab_aware(str);
    }

    public int[] split_dumb(String str) {

        if (str.isEmpty()) {
            return new int[]{0,0,0};
        }

        List<Integer> list = new ArrayList<>();

        int chunkStart=0;
        int strLength = str.length();

        while (chunkStart<strLength) {
            if (chunkStart + usableWidth < strLength) {
                list.add(chunkStart);
                list.add(usableWidth);
                list.add(usableWidth);
                chunkStart += usableWidth;
            } else {
                int remainder = strLength - chunkStart;
                list.add(chunkStart);
                list.add(remainder);
                list.add(remainder);
                chunkStart += remainder;
            }
        }

        return list.stream().mapToInt(i -> i).toArray();
    }

    public int[] split_dumb_tab_aware(String str) {

        if (str.isEmpty()) {
            return new int[]{0,0,0};
        }

        List<Integer> list = new ArrayList<>();

        int chunkStart=0;
        int expandedSubLineSize = 0;// sub string size in expanded characters (eg tabs)
        int subLineSize = 0; // sub string size in raw characters

        for (int i=0;i<str.length();i++) {
            char c = str.charAt(i);
            int charSize = 1;
            if (c=='\t') charSize = 4;

            // If adding a char would put us over the usableWidth, commit this section.
            if (expandedSubLineSize+charSize > usableWidth) {
                list.add(chunkStart);
                list.add(subLineSize);
                list.add(expandedSubLineSize);
                chunkStart += subLineSize;
                expandedSubLineSize=charSize;
                subLineSize=1;

            } else {
                expandedSubLineSize+=charSize;
                subLineSize+=1;
            }
        }

        if (subLineSize>0) {
            list.add(chunkStart);
            list.add(subLineSize);
            list.add(expandedSubLineSize);
        }

        return list.stream().mapToInt(i -> i).toArray();
    }
}
