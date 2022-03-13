package com.physmo;

import java.util.ArrayList;
import java.util.List;

// split types
//  none
//  hard split
//  word split

public class LineSplitter {

    int usableWidth;

    public LineSplitter(int usableWidth, int mode) {
        this.usableWidth=usableWidth;
    }


    // should we return array of line starts and lengths instead of individual strings?
    // returns a list of pairs:
    //   [i]   start of section
    //   [i+1] length of section (in raw chars)
    //   [i+2] length of section (expanded tabs)
    public int[] split(String str, int tabSize) {
        return split_dumb_tab_aware(str, tabSize);
    }

    public int[] split_dumb_tab_aware(String str, int tabSize) {

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
            if (c=='\t') charSize = tabSize;

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
