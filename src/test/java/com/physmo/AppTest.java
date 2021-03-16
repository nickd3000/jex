package com.physmo;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void misc() {
        File f = new File("/");
        File[] files = f.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

}
