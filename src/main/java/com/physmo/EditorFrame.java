package com.physmo;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.physmo.buffers.TextBuffer;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;

import java.io.IOException;


// The Main App
public class EditorFrame {

    Terminal terminal;
    Screen screen;
    TextGraphics tg;

    TextBuffer textBuffer;
    Viewport testViewport;

    Panel mainFrame;
    InfoBar infoBar;

    boolean running = true;

    public EditorFrame(Terminal terminal,
                       Screen screen,
                       TextGraphics tg) {

        mainFrame = new Panel() {
            @Override
            void draw(TextGraphics tg) {

            }
        };

        infoBar = new InfoBar(this);
        infoBar.setParent(mainFrame);
        infoBar.setPanelX(0);
        infoBar.setPanelY(tg.getSize().getRows()-1);
        infoBar.setHeight(1);
        infoBar.setWidth(tg.getSize().getColumns());

        this.terminal = terminal;
        this.screen = screen;
        this.tg = tg;

        initTestViewport(tg);
    }

    public static String faketextFile() {
        String str = "A text editor is a type of computer program that edits plain text.\n" +
                "Such programs are sometimes known as notepad software,\n" +
                "following the naming of Microsoft Notepad.\n" +
                "Text editors are provided with operating systems and software development packages,\n" +
                "and can be used to change files such as configuration files,\n" +
                "documentation files and programming language source code.\n";
        str=str+str;
        str=str+str;
        str=str+str;
        return str;
    }

    public void run() throws IOException, InterruptedException {

        while (running) {
            processInput();
            //ColorRepo.setDefaultTextColor(tg);
            //tg.fill(' ');
            testViewport.draw(tg);
            infoBar.draw(tg);
            setCursorPositionForView();
            screen.refresh();

            Thread.sleep(1000 / 60);

        }

    }

    private void setCursorPositionForView() throws IOException {
        screen.setCursorPosition(
                new TerminalPosition(
                        testViewport.getCurser().x + testViewport.getPanelX(),
                        testViewport.getCurser().y + testViewport.getPanelX()));

        terminal.setCursorVisible(true);
    }

    public void processInput() throws IOException {
        int count=0;
        KeyStroke keyStroke = terminal.pollInput();
        while (keyStroke!=null) {

            processKeyStroke(keyStroke);
            if (count>100) {
                return;
            }

            keyStroke = terminal.pollInput();
            count++;

        }
    }

    public void processKeyStroke(KeyStroke keyStroke) throws IOException {
        //KeyStroke keyStroke = terminal.readInput();
        if (keyStroke.getKeyType() == KeyType.Character) {
            Character character = keyStroke.getCharacter();
            int charPos = testViewport.getCurser().getDocumentIndex();
            textBuffer.insert(charPos, "" + character);
            testViewport.getCurser().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.Enter) {
            int charPos = testViewport.getCurser().getDocumentIndex();
            textBuffer.insert(charPos, "\n");
            testViewport.getCurser().moveRight();
            //testViewport.getCurser().y++;
            testViewport.getCurser().x = 0;
        }

        if (keyStroke.getKeyType() == KeyType.Delete) {
            int charPos = testViewport.getCurser().getDocumentIndex();
            textBuffer.deleteCharacter(charPos);
        }
        if (keyStroke.getKeyType() == KeyType.Backspace) {
            int charPos = testViewport.getCurser().getDocumentIndex();
            testViewport.getCurser().moveLeft();
            textBuffer.deleteCharacter(charPos-1);

        }

        if (keyStroke.getKeyType() == KeyType.Escape) {
            running = false;
        }

        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            testViewport.getCurser().moveLeft();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            testViewport.getCurser().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            testViewport.getCurser().moveUp();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            testViewport.getCurser().moveDown();
        }
    }

    public void initTestViewport(TextGraphics tg) {
        TerminalSize size = tg.getSize();
        testViewport = new Viewport();
        textBuffer = new PieceTableTextBuffer();
        textBuffer.setInitialtext(faketextFile());
        testViewport.setTextBuffer(textBuffer);
        testViewport.setHeight(size.getRows()-2);
        testViewport.setWidth(size.getColumns()-2);
        testViewport.setPanelX(1);
        testViewport.setPanelY(1);
        testViewport.draw(this.tg);
    }

}
