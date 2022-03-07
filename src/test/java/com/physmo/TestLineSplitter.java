package com.physmo;

import org.junit.Assert;
import org.junit.Test;

public class TestLineSplitter {

    @Test
    public void testBasicSplit() {
        LineSplitter ls = new LineSplitter(10,0);
        int[] splits = ls.split("111111111122222222223333333333444");
        printPairs(splits);
        Assert.assertTrue(splits[0]==0);
        Assert.assertTrue(splits[1]==10);
        Assert.assertTrue(splits[2]==10);
        Assert.assertTrue(splits[3]==10);
        Assert.assertTrue(splits[4]==20);
        Assert.assertTrue(splits[5]==10);
        Assert.assertTrue(splits[6]==30);
        Assert.assertTrue(splits[7]==3);
    }

    public static void printPairs(int[] vals) {
        int i=0;
        while (i<vals.length) {
            System.out.println(""+vals[i++] + ", "+ vals[i++]);
        }
    }
}
