package com.physmo.panels;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.physmo.Cursor;
import com.physmo.MainApp;
import com.physmo.Point;
import com.physmo.buffers.TextBuffer;

// TODO: rename to text viewport?
// Vieport contains:
//  text panel
//  scroll bar
public class Viewport extends Panel {

    int id;
    TextBuffer textBuffer;
    ScrollBar scrollBar;
    EditorPanel textPanel;
    MainApp mainApp;

    public Viewport(MainApp mainApp) {
        this.mainApp = mainApp;
        textPanel = new EditorPanel(mainApp);
        addChild(textPanel);

        scrollBar = new ScrollBar(mainApp, this);
        addChild(scrollBar);
    }

    public EditorPanel getTextPanel() {
        return textPanel;
    }

    public void doLayout() {
        textPanel.setPosition(0, 0);
        textPanel.setSize(this.size.x - 1, this.size.y);
        textPanel.setVisible(true);

        scrollBar.setPosition(this.size.x - 1, 0);
        scrollBar.setSize(1, this.size.y);
        scrollBar.setVisible(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getCursorPositionForDisplay() {
        return textPanel.getCursorPositionForDisplay();
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
        textPanel.setTextBuffer(textBuffer);
    }

    @Override
    public void draw(TextGraphics tg) {
        if (textBuffer == null) return;
        textPanel.draw(tg);
        //scrollBar.draw(tg);
    }

    @Override
    public boolean processKeystroke(KeyStroke keyStroke) {
//        return super.processKeystroke(keyStroke);

        if (keyStroke.getKeyType() == KeyType.Character) {
            TextBuffer textBuffer = getTextBuffer();
            Character character = keyStroke.getCharacter();
            int charPos = getCursor().getDocumentIndex();
            textBuffer.insert(charPos, "" + character);
            getCursor().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.Enter) {
            TextBuffer textBuffer = getTextBuffer();
            int charPos = getCursor().getDocumentIndex();
            textBuffer.insert(charPos, "\n");
            getCursor().moveRight();
            //testViewport.getCurser().y++;
            getCursor().x = 0;
        }

        if (keyStroke.getKeyType() == KeyType.Delete) {
            TextBuffer textBuffer = getTextBuffer();
            int charPos = getCursor().getDocumentIndex();
            textBuffer.deleteCharacter(charPos);
        }
        if (keyStroke.getKeyType() == KeyType.Backspace) {
            TextBuffer textBuffer = getTextBuffer();
            int charPos = getCursor().getDocumentIndex();
            getCursor().moveLeft();
            textBuffer.deleteCharacter(charPos - 1);

        }
        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            getCursor().moveLeft();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            getCursor().moveRight();
        }
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            getCursor().moveUp(1);
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            getCursor().moveDown(1);
        }
        if (keyStroke.getKeyType() == KeyType.PageUp) {
            getCursor().moveUp(10);
        }
        if (keyStroke.getKeyType() == KeyType.PageDown) {
            getCursor().moveDown(10);
        }
        if (keyStroke.getKeyType() == KeyType.End) {
            getCursor().jumpToEndOfLine();
        }
        if (keyStroke.getKeyType() == KeyType.Home) {
            getCursor().jumpToStartOfLine();
        }
        return false;
    }

    public Cursor getCursor() {
        return textPanel.getCursor();
    }

    public TextBuffer getTextBuffer() {
        return this.textBuffer;
    }
}
