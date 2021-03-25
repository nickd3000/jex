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
import com.physmo.panels.FilePanel;
import com.physmo.panels.MainFrame;
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
    MainFrame mainFrame;

    Settings settings = new Settings();
    boolean running = true;
    int activeViewportId;
    ColorRepo colorRepo = new ColorRepo();
    ViewPortRepo viewPortRepo = new ViewPortRepo();
    DocumentRepo documentRepo = new DocumentRepo();
    boolean pendingResize = false;
    int pendingWidth = 0;
    int pendingHeight = 0;

    FilePanel fileOpenPanel;
    MainStates currentState = MainStates.NORMAL;

    public MainApp(Terminal terminal,
                   Screen screen,
                   TextGraphics tg, String initialFilePath) {

        mainFrame = new MainFrame(this, tg.getSize().getColumns(), tg.getSize().getRows());

        this.terminal = terminal;
        this.screen = screen;
        this.tg = tg;

        settings.initialFilePath = initialFilePath;

        loadFile(tg, settings.initialFilePath);

        fileOpenPanel = new FilePanel();
        fileOpenPanel.setSize(60, 16);
        fileOpenPanel.setPosition(5, 5);
        fileOpenPanel.doLayout();
        fileOpenPanel.addLoadFileCallback(o -> {
            System.out.println("MainApp:"+(String)o);
            loadFile(tg, (String)o);
            changeState(MainStates.NORMAL);
        });
    }

    public void loadFile(TextGraphics tg, String path) {
        // Create viewport.
        int viewportId = viewPortRepo.createViewport(this);
        Viewport vp = viewPortRepo.getViewportById(viewportId);
        activeViewportId = viewportId;

        // Create document
        int documentId = documentRepo.createEmptyDocument();
        DocumentContainer dc = documentRepo.getDocumentById(documentId);


        // Create text buffer
        TerminalSize size = tg.getSize();
        textBuffer = new PieceTableTextBuffer();

        if (path != null) {
            textBuffer.setInitialtext(loadFile(path));
        } else {
            textBuffer.setInitialtext(faketextFile());
        }

        // Attach text buffer to document
        dc.setTextBuffer(textBuffer);

        vp.setTextBuffer(textBuffer);

        mainFrame.addViewport(vp);

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

    public String loadFile(String path) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public ColorRepo getColorRepo() {
        return colorRepo;
    }

    // Resize handler can be called at any causing update anomolies, we want to handle the resize when we are ready.
    public void queueResize(int screenWidth, int screenHeight) {
        pendingResize = true;
        pendingWidth = screenWidth;
        pendingHeight = screenHeight;
    }

    // TODO: this should all be event based
    public void run() throws IOException, InterruptedException {

        while (running) {
            resizeIfQueued();

            tg = screen.newTextGraphics();

            processInput();

            if (getActiveViewport() != null) {
                getActiveViewport().drawChildren(tg, true);
            }

            //mainFrame.drawChildren(tg, true);
            mainFrame.draw(tg);
            setCursorPositionForView();

            if (currentState==MainStates.FILE_OPEN) {
                fileOpenPanel.drawChildren(tg, true);
            }

            screen.doResizeIfNecessary();
            screen.refresh();

            Thread.sleep(1000 / 60);

            // Hack
            //updateOnResize(tg);
        }

    }

    public void resizeIfQueued() {

        if (pendingResize == false) return;
        pendingResize = false;
        mainFrame.setPosition(0, 0);
        mainFrame.setSize(pendingWidth, pendingHeight);

        mainFrame.doLayout();


        // Test viewport
//        Viewport vp = getActiveViewport();

//        vp.setPosition(0, 0);
//        vp.setSize(pendingWidth, pendingHeight);

//        vp.doLayout();
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
            if (count > 100) { // avoid looping forever
                return;
            }

            keyStroke = terminal.pollInput();
            count++;

        }
    }

    public boolean processKeyStroke(KeyStroke keyStroke) throws IOException {
        Viewport activeViewport = getActiveViewport();


        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.isCtrlDown()) {
            if (keyStroke.getCharacter() == 'c') {
                running = false;
                return true;
            }
        }

        if (currentState==MainStates.NORMAL) {
            return mainFrame.processKeystroke(keyStroke);
        }
        else if (currentState==MainStates.FILE_OPEN) {
            return fileOpenPanel.processKeystroke(keyStroke);
        }

        return false;
    }

    public Viewport getActiveViewport() {
        return viewPortRepo.getViewportById(activeViewportId);
        //return testViewport;
    }

    public void commandReceiver(String command, Object data) {
        if (command.equals(Commands.FILE_EXIT)) {
            running = false;
        }

        if (command.equals(Commands.FILE_OPEN)) {
            changeState(MainStates.FILE_OPEN);
        }
    }

    public void changeState(MainStates newState) {
        if (currentState==MainStates.NORMAL) {
            if (newState==MainStates.FILE_OPEN) {
                System.out.println("making file panel visible");
                fileOpenPanel.setVisible(true);
                currentState=MainStates.FILE_OPEN;
            }
        }

        if (currentState==MainStates.FILE_OPEN) {
            if (newState==MainStates.NORMAL) {
                fileOpenPanel.setVisible(false);
                currentState=MainStates.NORMAL;
            }
        }

    }
}
