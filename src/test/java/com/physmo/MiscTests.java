package com.physmo;

import org.junit.Test;

import java.io.File;

public class MiscTests {

    @Test
    public void misc() {
        File f = new File("/tmp");
        File[] files = f.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    @Test
    public void t2() {
        String s1 = "hello";
        String s2 = "hello\n";
        System.out.println(s1.length());
        System.out.println(s2.length());

    }

    @Test
    public void testTabReplace() {
        String str = "helo\t\t\tgoodbye";
        System.out.println(str);
        str = str.replace("\t", "|---|");
        System.out.println(str);
    }

    @Test
    public void detectTabCharacter() {
        char c = "\t\tnnn".charAt(0);
        int charSize = 1;
        if (c=='\t') System.out.println("found tab");
    }
}
