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
import com.physmo.document.DocumentContainer;
import com.physmo.document.DocumentRepo;
import com.physmo.panels.InfoBar;
import com.physmo.panels.Panel;
import com.physmo.panels.Viewport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


// The Main App
public class MainApp {


    public TextBuffer textBuffer;
    Terminal terminal;
    Screen screen;
    TextGraphics tg;
    //Viewport testViewport;
    Panel mainFrame;
    InfoBar infoBar;
    Settings settings = new Settings();
    boolean running = true;
    int activeViewportId;

    ViewPortRepo viewPortRepo = new ViewPortRepo();
    DocumentRepo documentRepo = new DocumentRepo();

    boolean pendingResize = false;
    int pendingWidth = 0;
    int pendingHeight = 0;

    public MainApp(Terminal terminal,
                   Screen screen,
                   TextGraphics tg, String initialFilePath) {

        mainFrame = new Panel() {
            @Override
            public void draw(TextGraphics tg) {

            }
        };

        infoBar = new InfoBar(this);
        infoBar.setParent(mainFrame);
        infoBar.setPanelX(0);
        infoBar.setPanelY(tg.getSize().getRows() - 1);
        infoBar.setHeight(1);
        infoBar.setWidth(tg.getSize().getColumns());

        this.terminal = terminal;
        this.screen = screen;
        this.tg = tg;

        settings.initialFilePath = initialFilePath;

        initTestViewport(tg);
    }

    public static String faketextFile() {
        String str = "A text editor is a type of computer program that edits plain text.\n" +
                "Such programs are sometimes known as notepad software,\n" +
                "following the naming of Microsoft Notepad.\n" +
                "Text editors are provided with operating systems and software development packages,\n" +
                "and can be used to change files such as configuration files,\n" +
                "documentation files and programming language source code.\n";
        str = str + str;
        str = str + str;
        str = str + str;
        return str;
    }

    // Resize handler can be called at any causing update anomolies, we want to handle the resize when we are ready.
    public void queueResize(int screenWidth, int screenHeight) {
        pendingResize = true;
        pendingWidth = screenWidth;
        pendingHeight = screenHeight;
    }

    public void resizeIfQueued() {

        if (pendingResize == false) return;
        pendingResize = false;

        mainFrame.setPanelX(0);
        mainFrame.setPanelY(0);
        mainFrame.setHeight(pendingHeight);
        mainFrame.setWidth(pendingWidth);


        // Info Bar
        infoBar.setParent(mainFrame);
        infoBar.setPanelX(0);
        infoBar.setPanelY(pendingHeight - 1);
        infoBar.setHeight(1);
        infoBar.setWidth(pendingWidth);

        // Test viewport
        Viewport vp = getActiveViewport();
        vp.setParent(mainFrame);
        vp.setHeight(pendingHeight);
        vp.setWidth(pendingWidth);
        vp.setPanelX(0);
        vp.setPanelY(0);
        vp.doLayout();
    }

    public void run() throws IOException, InterruptedException {

        while (running) {
            resizeIfQueued();

            tg = screen.newTextGraphics();

            processInput();
            //ColorRepo.setDefaultTextColor(tg);
            //tg.fill(' ');

            if (getActiveViewport() != null) getActiveViewport().draw(tg);
            //testViewport.draw(tg);

            infoBar.draw(tg);
            setCursorPositionForView();

            screen.doResizeIfNecessary();
            screen.refresh();

            Thread.sleep(1000 / 60);

            // Hack
            //updateOnResize(tg);
        }

    }

    private void setCursorPositionForView() throws IOException {
        Viewport vp = getActiveViewport();
        Point cpos = vp.getCursorPositionForDisplay();

        screen.setCursorPosition(
                new TerminalPosition(cpos.x, cpos.y));

        terminal.setCursorVisible(true);
    }

    public void processInput() throws IOException {
        int count = 0;
        KeyStroke keyStroke = terminal.pollInput();
        while (keyStroke != null) {

            processKeyStroke(keyStroke);
            if (count > 100) {
                return;
            }

            keyStroke = terminal.pollInput();
            count++;

        }
    }

    public void processKeyStroke(KeyStroke keyStroke) throws IOException {
        Viewport activeViewport = getActiveViewport();

        //KeyStroke keyStroke = terminal.readInput();
        if (keyStroke.getKeyType() == KeyType.Character) {
            Character character = keyStroke.getCharacter();
            int charPos = activeViewport.getCursor().getDocumentIndex();
            textBuffer.insert(charPos, "" + character);
            activeViewport.getCursor().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.Enter) {
            int charPos = activeViewport.getCursor().getDocumentIndex();
            textBuffer.insert(charPos, "\n");
            activeViewport.getCursor().moveRight();
            //testViewport.getCurser().y++;
            activeViewport.getCursor().x = 0;
        }

        if (keyStroke.getKeyType() == KeyType.Delete) {
            int charPos = activeViewport.getCursor().getDocumentIndex();
            textBuffer.deleteCharacter(charPos);
        }
        if (keyStroke.getKeyType() == KeyType.Backspace) {
            int charPos = activeViewport.getCursor().getDocumentIndex();
            activeViewport.getCursor().moveLeft();
            textBuffer.deleteCharacter(charPos - 1);

        }

        if (keyStroke.getKeyType() == KeyType.Escape) {
            running = false;
        }

        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            activeViewport.getCursor().moveLeft();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            activeViewport.getCursor().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            activeViewport.getCursor().moveUp(1);
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            activeViewport.getCursor().moveDown(1);
        }

        if (keyStroke.getKeyType() == KeyType.PageUp) {
            activeViewport.getCursor().moveUp(10);
        }
        if (keyStroke.getKeyType() == KeyType.PageDown) {
            activeViewport.getCursor().moveDown(10);
        }
        if (keyStroke.getKeyType() == KeyType.End) {
            activeViewport.getCursor().jumpToEndOfLine();
        }
        if (keyStroke.getKeyType() == KeyType.Home) {
            activeViewport.getCursor().jumpToStartOfLine();
        }
    }

    public void initTestViewport(TextGraphics tg) {
        // Create viewport.
        int viewportId = viewPortRepo.createViewport();
        Viewport vp = viewPortRepo.getViewportById(viewportId);
        activeViewportId = viewportId;

        // Create document
        int documentId = documentRepo.createEmptyDocument();
        DocumentContainer dc = documentRepo.getDocumentById(documentId);


        // Create text buffer
        TerminalSize size = tg.getSize();
        textBuffer = new PieceTableTextBuffer();

        if (settings.initialFilePath != null) {
            textBuffer.setInitialtext(loadFile(settings.initialFilePath));
        } else {
            textBuffer.setInitialtext(faketextFile());
        }

        // Attech text buffer to document
        dc.setTextBuffer(textBuffer);

        vp.setTextBuffer(textBuffer);
        vp.setHeight(size.getRows() - 2);
        vp.setWidth(size.getColumns());
        vp.setPanelX(0);
        vp.setPanelY(1);
        vp.doLayout();
        vp.draw(this.tg);
    }

    public String loadFile(String path) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public Viewport getActiveViewport() {
        return viewPortRepo.getViewportById(activeViewportId);
        //return testViewport;
    }
}
