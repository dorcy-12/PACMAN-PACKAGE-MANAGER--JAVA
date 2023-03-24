package de.tukl.programmierpraktikum2021.P2;
import de.tukl.programmierpraktikum2021.p1.a1.DuplicateEdgeException;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p2.a1.GraphExtendedImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphExtendedTest {
    @Test
    public void GraphExtendendImpl() throws InvalidNodeException, DuplicateEdgeException {
        GraphExtendedImpl<Integer> sg = new GraphExtendedImpl<>();
        sg.addNode("A",1);
        sg.addNode("B", 2);
        sg.addNode("C", 3);
        sg.addNode("D",4);
        sg.addEdge("A", "B");
        sg.addEdge("B","C");

        // has cycle
        assertFalse(sg.hasCycle(), "Der Graph muss mindestens ein Cycle enthalten");
        sg.addEdge("C", "A");
        assertTrue(sg.hasCycle(), "Graph hat Zykel A->B->C");

        // isconnected
        assertFalse(sg.isConnected(), "Es gibt keinen Weg zu D");
        sg.addEdge("D","A");
        sg.addEdge("C", "D");
        assertTrue(sg.isConnected());

        //breadthfirstsearch
        List<String> bfs = sg.breadthFirstSearch("A","C");

        assertArrayEquals(new String[] {"A","B","C"}, bfs.toArray(new String[0]));

        sg.addEdge("A", "C"); // Es gibt einen neuen kürzeren Weg zu C von A

        List<String> bfs2 = sg.breadthFirstSearch("A","C");
        assertArrayEquals(new String[] {"A","C"}, bfs2.toArray(new String[0]));

        sg.addNode("E", 5);

        List<String> bfs3 = sg.breadthFirstSearch("A","E");
        assertEquals(0, bfs3.size(), "Es gibt keine Kante, die zu E führt");

        assertThrows(InvalidNodeException.class, ()->sg.breadthFirstSearch("X", "A"));
        assertThrows(InvalidNodeException.class, ()->sg.breadthFirstSearch("A", "X"));
        assertThrows(InvalidNodeException.class, ()->sg.breadthFirstSearch("X", "Z"));




    }
}
