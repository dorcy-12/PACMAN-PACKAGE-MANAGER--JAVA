package de.tukl.programmierpraktikum2021.p2.a1;

import de.tukl.programmierpraktikum2021.p1.a1.Graph;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;

import java.util.List;

public interface GraphExtended<D> extends Graph<D> {
    /**
     * Checks if the graph contains a cycle.
     *
     * @return true if the graph contains a cycle, false otherwise.
     */
    boolean hasCycle() throws InvalidNodeException;

    /**
     * Checks if the graph is connected.
     *
     * @return true if the graph is connected.
     */
    boolean isConnected() throws InvalidNodeException;

    /**
     * Perform a breadth-first search.
     * The result is a shortest (by number of intermediate nodes) directed path from node fromId to node toId.
     *
     * @param fromId node Id to start at
     * @param toId Id of the ending node
     * @return Path, i.e. List of node Ids from node fromId to node toId
     * @throws InvalidNodeException when fromId does not exist in the graph
     */
    List<String> breadthFirstSearch(String fromId, String toId) throws InvalidNodeException;
}
