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
}
