package com.physmo.buffers.piecetable;

import com.physmo.buffers.TextBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PieceTableTextBuffer extends TextBuffer {

    String bufferOriginal = "";
    String buffer1 = "";
    LinkedList<Node> nodes = new LinkedList<>();

    public PieceTableTextBuffer() {

    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public void setInitialtext(String text) {
        bufferOriginal = text;
        Node firstNode = new Node(0, bufferOriginal.length(), 0);
        calculateLineStartsForNode(firstNode, true);
        nodes.add(firstNode);
    }

    @Override
    public String getLine(int lineNumber) {
        int trackLine = 0;
        String lineText = "";


        for (Node node : nodes) {
            int numLinesInNode = node.lineStarts.size();

            if (lineNumber >= trackLine && lineNumber < trackLine + node.lineStarts.size()) {
                lineText = extractLineFromPosition(node, node.lineStarts.get(lineNumber - trackLine));
            }
            trackLine += numLinesInNode;
        }

        return lineText;
    }

    public String extractLineFromPosition(Node inNode, int offset) {

        boolean tracking = false;
        String str = "";
        String buffer = null;
        int readHead = 0;

        for (Node node : nodes) {
            if (node != inNode && tracking == false) continue;

            // Start tracking
            if (tracking == false) {
                tracking = true;
                buffer = getBufferById(node.bufferId);
                readHead = node.start + offset;
            } else {
                buffer = getBufferById(node.bufferId);
                readHead = node.start;// + offset;
            }

            if (tracking == true) {
                for (int i = readHead; i < node.start + node.length; i++) {
                    char c = buffer.charAt(i);
                    if (isLineEnd(c)) {
                        return str;
                    }
                    str += c;
                }
            }
        }

        return str;
    }

    @Override
    public String getCharacter(int position) {
        PieceFindResult result = findPieceForPosition(position);

        String b = getBufferById(result.node.bufferId);
        return "" + b.charAt(result.node.start + result.offset);

    }

    /*

    text should be inserted before the character id, if we insert one character, it will now exist at specified location.

    what different situations can we have?
        insert at start of piece
        insert at end of piece
        insert inside piece

        maybe split should only do work if inside a piece
        then we just insert at the originally requested position
        the insert would have to be at the start or end of an existing piece, not inside one
     */
    @Override
    public void insert(int position, String text) {
        PieceFindResult result = findPieceForPosition(position);
        int resultIndex = nodes.indexOf(result.node);

        //String buffer = getBufferById(1);
        int bufferLength = buffer1.length();
        buffer1 = buffer1 + text;

        // Create new node from input text.
        Node newNode = new Node();
        newNode.bufferId = 1;
        newNode.start = bufferLength;
        newNode.length = text.length();
        calculateLineStartsForNode(newNode, false);

        if (result.endOfBuffer) {
            insertAtEnd(text);
            return;
        }

        // Split found node
        if (result.offset > 0) {
            splitNode(result.node, result.offset);

            // Now perform the find again
            result = findPieceForPosition(position);
            resultIndex = nodes.indexOf(result.node);
        }

        // Insert before located node.
        if (result.offset == 0) {
            //System.out.println("Inserting before located node.");

            // Does the node we are inserting before have a line start at the beginning?
            // If so we need to move the initial line start from the existing node to the new one.
            if (result.node.lineStarts.size() > 0) {
                if (result.node.lineStarts.get(0) == 0) {
                    calculateLineStartsForNode(newNode, true);

                    // The new node takes over the line start from the next one
                    calculateLineStartsForNode(result.node, false);
                }
            }

            nodes.add(resultIndex, newNode);
        }

    }


    public void insertAtEnd(String text) {
        String buffer = getBufferById(1);
        int bufferLength = buffer1.length();
        buffer1 = buffer + text;

        // Create new node from input text.
        Node newNode = new Node();
        newNode.bufferId = 1;
        newNode.start = bufferLength;
        newNode.length = text.length();
        calculateLineStartsForNode(newNode, false);

        // todo: use addlast?
        int index = nodes.indexOf(nodes.getLast());
        ListIterator<Node> nodeListIterator = nodes.listIterator(index);
        nodeListIterator.next();
        nodeListIterator.add(newNode);
    }

    @Override
    public int getLineCount() {
        int lineCount = 0;
        for (Node node : nodes) {
            lineCount += node.lineStarts.size();
        }
        return lineCount;
    }

    // TODO:
    @Override
    public int getStartOfLineIndex(int lineNumber) {
        int trackLine = 0;
        String lineText = "";
        int charCount = 0;

        for (Node node : nodes) {
            int numLinesInNode = node.lineStarts.size();

            if (lineNumber >= trackLine && lineNumber < trackLine + node.lineStarts.size()) {
                // the line is in this node.
                int matchedLine = lineNumber - trackLine;

                charCount += node.lineStarts.get(matchedLine);
                return charCount;
                //lineText = extractLineFromPosition(node, node.lineStarts.get(lineNumber - trackLine));
            }
            charCount += node.length;
            trackLine += numLinesInNode;
        }
        return 0;
    }

    @Override
    public void deleteCharacter(int position) {
        PieceFindResult result = findPieceForPosition(position);
        int resultIndex = nodes.indexOf(result.node);

        // Split found node
        if (result.offset > 0) {
            splitNode(result.node, result.offset);
            result = findPieceForPosition(position);
        }

        if (result.endOfBuffer) {
            result.node.length--;
            calculateLineStartsForNode(result.node, false);
            return;
        }

        // Insert before located node.
        if (result.offset == 0) {
            result.node.start++;
            result.node.length--;
            calculateLineStartsForNode(result.node, false);
        }

    }

    public String getBufferById(int id) {
        if (id == 1) return buffer1;
        else return bufferOriginal;
    }

    public PieceFindResult findPieceForPosition(int position) {
        PieceFindResult result = new PieceFindResult();

        int rollingPosition = 0;
        for (Node node : nodes) {
            int nextPosition = rollingPosition + node.length;
            if (position >= rollingPosition && position < nextPosition) {
                result.node = node;
                result.piecePosition = rollingPosition;
                result.offset = position - rollingPosition;
                return result;
            }
            rollingPosition = nextPosition;
        }

        result.endOfBuffer = true;

        return result;
    }

    // linux = \n  windows = \r\n
    public void calculateLineStartsForNode(Node node, boolean addLineStartToBeginning) {
        node.lineStarts.clear();
        if (addLineStartToBeginning) node.lineStarts.add(0);

        String buffer = getBufferById(node.bufferId);
        int end = node.start + node.length;
        for (int i = node.start; i < end; i++) {
            if (isLineEnd(buffer.charAt(i))) {
                node.lineStarts.add(i - node.start + 1);
            }
        }
    }

    public boolean isLineEnd(char c) {
        return c == '\n';
    }

    // returns first of the pair of nodes that are created
    public Node splitNode(Node node, int midPoint) {


        Node newA = new Node();
        newA.bufferId = node.bufferId;
        newA.start = node.start;
        newA.length = midPoint;
        boolean lineAtStart = false;
        if (node.lineStarts.size() > 0) {
            if (node.lineStarts.get(0) == 0) lineAtStart = true;
        }
        calculateLineStartsForNode(newA, lineAtStart);

        Node newB = new Node();
        newB.bufferId = node.bufferId;
        newB.start = node.start + midPoint;
        newB.length = node.length - midPoint;

        calculateLineStartsForNode(newB, false);

        int nodeIndex = nodes.indexOf(node);
        ListIterator<Node> nodeListIterator = nodes.listIterator(nodeIndex);
        nodeListIterator.next();
        nodeListIterator.remove();

        if (newA.length > 0)
            nodeListIterator.add(newA);
        if (newB.length > 0)
            nodeListIterator.add(newB);

        // If this section had a line at start but the left of the split is too small, add line start to right side of split?
        if (lineAtStart && newA.length == 0) {
            newB.lineStarts.add(0);
        }

        if (newA.length > 0)
            return newA;
        else
            return newB;

        // remove node
        // create new ones
        // add two new ones
    }
}
