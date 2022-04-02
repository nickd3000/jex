package com.physmo;

import org.junit.Assert;
import org.junit.Test;

public class TestLineSplitter {

    @Test
    public void testBasicSplit() {
        LineSplitter ls = new LineSplitter(10,0);
        int[] splits = ls.split("111111111122222222223333333333444", 8);
        printTriples(splits);
        Assert.assertTrue(splits[0]==0);
        Assert.assertTrue(splits[1]==10);
        Assert.assertTrue(splits[3]==10);
        Assert.assertTrue(splits[4]==10);
        Assert.assertTrue(splits[6]==20);
        Assert.assertTrue(splits[7]==10);
        Assert.assertTrue(splits[9]==30);
        Assert.assertTrue(splits[10]==3);
    }



    @Test
    public void testFindLengthOfNextWord() {
        LineSplitter ls = new LineSplitter(10,0);
        String testString = "one two three";

        Assert.assertEquals(3, ls.findLengthOfNextWord(testString, 0, 10));
        Assert.assertEquals(2, ls.findLengthOfNextWord(testString, 1, 10));
        Assert.assertEquals(3, ls.findLengthOfNextWord(testString, 4, 10));
        Assert.assertEquals(5, ls.findLengthOfNextWord(testString, 8, 10));
        Assert.assertEquals(1, ls.findLengthOfNextWord(testString, 12, 10));
        Assert.assertEquals(0, ls.findLengthOfNextWord(testString, 13, 10));

        //System.out.println(lengthOfNextWord);

    }


    @Test
    public void testLineSplitterSplitOnWords() {

        //testLineSplitterSplitOnWordsForString("55555 4444 0000000000 333", 20);
        //testLineSplitterSplitOnWordsForString("Now is the time for all good men to come to the aid of the party.",10);

        testLineSplitterSplitOnWordsForString("333 333 1616161616161616 4444", 8);

    }

    public void testLineSplitterSplitOnWordsForString(String str, int width) {
        LineSplitter ls = new LineSplitter(width,0);
        int[] splits = ls.split_on_words(str, 8);
        System.out.println(str);
        printTriples(splits);
        printSubLines(str, splits);
    }

    public static void printTriples(int[] vals) {
        int i=0;
        while (i<vals.length) {
            System.out.println(""+vals[i++] + ", "+ vals[i++] + ", "+ vals[i++]);
        }
    }

    public static void printSubLines(String str, int[] vals) {
        for (int i=0;i<vals.length/3;i++) {
            int start = vals[i*3];
            int length = vals[(i*3)+1];
            System.out.println("["+str.substring(start, start+length)+"]");
        }
    }
}
