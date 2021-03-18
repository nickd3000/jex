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

}
