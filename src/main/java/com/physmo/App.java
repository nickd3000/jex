package com.physmo;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.physmo.buffers.TextBuffer;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;

import java.io.IOException;


/**
 * Hello world!
 */
public class App {

    Terminal terminal;
    Screen screen;
    TextGraphics tg;
    EditorFrame editorFrame;

    public static void main(String[] args) {

        App app = new App();
        app.start();

    }

    public App() {
        try {
            initLanterna();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        editorFrame = new EditorFrame(terminal, screen, tg);
        try {
            editorFrame.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void initLanterna() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        tg = screen.newTextGraphics();
        screen.startScreen();
    }

//    public static void testWindow() throws IOException, InterruptedException {
//
//        //write a text
//        tg.putString(1, 1, "1,1");
//        tg.putString(10, 10, "10,10");
//        tg.putString(20, 20, "20,20");
//
//        Viewport viewport = new Viewport();
//        TextBuffer textBuffer = new PieceTableTextBuffer();
//        textBuffer.setInitialtext(faketextFile());
//        viewport.setTextBuffer(textBuffer);
//        viewport.setHeight(20);
//        viewport.setWidth(20);
//        viewport.setX(2);
//        viewport.setY(2);
//        viewport.draw(tg);
//
//        screen.refresh();
//        Thread.sleep(1000 * 10);
//    }


}
