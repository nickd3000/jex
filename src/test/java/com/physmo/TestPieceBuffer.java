package com.physmo;

import com.physmo.buffers.piecetable.Node;
import com.physmo.buffers.piecetable.PieceFindResult;
import com.physmo.buffers.piecetable.PieceTableTextBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ListIterator;

public class TestPieceBuffer {
    @Test
    public void test1() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("12345678901234567890");

        PieceFindResult p = pt.findPieceForPosition(5);

        System.out.println(p);
    }

    @Test
    public void test2() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("12345678901234567890");

        for (int i = 0; i < 20; i++) {
            System.out.print(pt.getCharacter(i));
        }
    }

    @Test
    public void testCalculateLineStartsForNode() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaa\nbbb\nccc\nddd");

        for (int i = 0; i < 10; i++) {
            System.out.print(pt.getCharacter(i));
        }
    }

    @Test
    public void testGetLineBasic() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaa\nbbb\nccc\nddd");

        String line0 = pt.getLine(0);
        String line1 = pt.getLine(1);
        String line2 = pt.getLine(2);

        System.out.println(line0);
        System.out.println(line1);
        System.out.println(line2);

        Assert.assertEquals("aaa", line0);
        Assert.assertEquals("bbb", line1);
        Assert.assertEquals("ccc", line2);
    }

    @Test
    public void testLinkedList() {
        LinkedList<String> list = new LinkedList<>();
        String three = "3333";
        list.add("1111");
        list.add("2222");
        list.add(three);
        list.add("4444");
        list.get(1);

        int nodeIndex = list.indexOf(three);
        ListIterator<String> nodeListIterator = list.listIterator();

        //

        System.out.println(nodeListIterator.next());
        System.out.println(nodeListIterator.next());


    }

    @Test
    public void testSplit() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("abcd\n1234");
        Node node = pt.getNodes().get(0);

        pt.splitNode(node, 2);
        pt.splitNode(pt.getNodes().get(1), 2);

//        System.out.println(pt.getLine(0));
//        System.out.println(pt.getLine(1));

        printAllLines(pt);
    }

    public void printAllLines(PieceTableTextBuffer pt) {
        int lineCount = pt.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            System.out.println(pt.getLine(i));
        }
    }

    @Test
    public void testInsertSimple() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("1234567890");
        pt.insert(5, "abcd");
        //pt.insert(7,"---");
        //System.out.println(pt.getLine(0));
        //System.out.println(pt.getLine(1));
        printAllLines(pt);
    }

    @Test
    public void testLineCount() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaa\nbbb\nccc\nddd");
        int lineCount = pt.getLineCount();
        System.out.println(lineCount);
        printAllLines(pt);
    }

    @Test
    public void testInsert2() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaaaaa");
        pt.insert(5, "bbb");
        pt.insert(5, "ccc");
        pt.insert(5, "ddd");
        pt.insert(5, "eee");
        pt.insert(5, "fff");

        printAllLines(pt);
    }

    @Test
    public void testInsertAtStart() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaaaaa");
        pt.insert(0, "bbb");

        printAllLines(pt);
    }

    @Test
    public void testInsertAtEnd() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaaaaa");
        pt.insert(6, "bbb");

        printAllLines(pt);
    }

    @Test
    public void testInsertMultiple() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("a");
        pt.insert(1, "b");
        pt.insert(0, "c");
        pt.insert(3, "d");

        printAllLines(pt);
    }

    @Test
    public void testInsertStartMiddleEnd() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("original");

        pt.insert(8, "END");
        pt.insert(0, "START");
        pt.insert(9, "MIDDLE");

        String line = pt.getLine(0);
        Assert.assertEquals("STARTorigMIDDLEinalEND", line);

        printAllLines(pt);
    }

    @Test
    public void testMultipleLinesAndInsert() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("This is a longer line.\nThis is the next line.\nAnd now one last line.");

        pt.insert(28, "****");

        String line = pt.getLine(0);
        //Assert.assertEquals("STARTorigMIDDLEinalEND", line);

        printAllLines(pt);
    }

    @Test
    public void testInsertReturn() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("aaaaaa");
        pt.insert(3, "\n");

        printAllLines(pt);
    }

    @Test
    public void testDeleteCharacter() {
        PieceTableTextBuffer pt = new PieceTableTextBuffer();
        pt.setInitialtext("123456");
        pt.deleteCharacter(3);

        printAllLines(pt);
    }
}
