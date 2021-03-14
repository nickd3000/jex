package com.physmo;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.physmo.panels.Panel;
import com.physmo.panels.TestPanel;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class TestScreen {

    @Test
    @Ignore
    public void mainTest() throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);

        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();

        //write a text
        tg.putString(1, 1, "1,1");
        tg.putString(10, 10, "10,10");
        tg.putString(20, 20, "20,20");

        test1(tg);
        test2(tg);
        test3(tg);
        screen.refresh();
        Thread.sleep(1000 * 10);
    }


    public static void test1(TextGraphics tg) {
        TerminalSize size = tg.getSize();
        int w = size.getColumns();
        int h = size.getRows();
        tg.putString(w - 1, h - 1, "*");
    }

    public static void test2(TextGraphics tg) {
        int y = 5;
        tg.setForegroundColor(TextColor.ANSI.BLUE);
        tg.putString(5, y++, "ANSI.BLUE");
        tg.setForegroundColor(TextColor.ANSI.RED);
        tg.putString(5, y++, "ANSI.RED");
        tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        tg.putString(5, y++, "ANSI.YELLOW_BRIGHT");
        tg.setForegroundColor(new TextColor.RGB(100, 200, 30));
        tg.putString(5, y++, "ANSI.YELLOW_BRIGHT");
        tg.setForegroundColor(new TextColor.RGB(130, 200, 30));
        tg.putString(5, y++, "ANSI.YELLOW_BRIGHT");
    }

    public static void test3(TextGraphics tg) {
        Panel p1 = new TestPanel();
        p1.setPosition(5, 5);
        p1.setSize(5, 5);
        ((TestPanel) p1).draw(tg);
    }
}
