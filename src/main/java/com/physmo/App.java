package com.physmo;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.physmo.document.DocumentRepo;

import java.io.IOException;

public class App {

    Terminal terminal;
    Screen screen;
    TextGraphics tg;
    MainApp editorFrame;
    ColorRepo colorRepo;
    ViewPortRepo viewPortRepo;
    DocumentRepo documentRepo;

    public App() {
        try {
            initLanterna();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initLanterna() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        tg = screen.newTextGraphics();
        screen.startScreen();
        colorRepo = new ColorRepo();
        viewPortRepo = new ViewPortRepo();
        documentRepo = new DocumentRepo();

        terminal.addResizeListener((terminal1, terminalSize) -> {

        });
    }

    public static void main(String[] args) {

        String startingFileName = null;
        if (args.length > 0) {
            startingFileName = args[0];
        }

        App app = new App();
        app.start(startingFileName);
    }

    public void start(String initialFileName) {
        editorFrame = new MainApp(terminal, screen, tg, colorRepo, viewPortRepo, documentRepo, initialFileName);
        terminal.addResizeListener((terminal1, terminalSize) -> {
            editorFrame.queueResize(terminalSize.getColumns(), terminalSize.getRows());
        });

        try {
            editorFrame.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
