package de.tukl.programmierpraktikum2021.p1.a1;

import java.util.Set;

/**
 * An interface for directed graphs.
 *
 * @param <D> Type for the data that is stored in the nodes.
 */
public interface Graph<D> {
    /**
     * Add a new node to the graph.
     *
     * @param data the data that should be stored in the new node
     */
    void addNode(String name, D data);

    /**
     * Get the data that is stored in a node.
     *
     * @param nodeId identifier of the node to read
     * @return the data for the node
     * @throws InvalidNodeException if nodeId is invalid
     */
    D getData(String nodeId) throws InvalidNodeException;

    /**
     * Set the data that is stored in a node.
     *
     * @param nodeId identifier of the node to update
     * @param data   the updated data for the node
     * @throws InvalidNodeException if nodeId is invalid
     */
    void setData(String nodeId, D data) throws InvalidNodeException;

    /**
     * Add a new directed edge to the graph.
     *
     * @param fromId identifier of the originating node
     * @param toId   identifier of the target node
     * @throws InvalidNodeException   if fromId or toId are invalid
     * @throws DuplicateEdgeException if the graph already contains an edge from node fromId to node toId
     */
    void addEdge(String fromId, String toId) throws InvalidNodeException, DuplicateEdgeException;

    /**
     * Get the identifiers of all nodes that are present in the graph.
     *
     * @return a set of identifiers
     */
    Set<String> getNodeIds();

    /**
     * Get the identifiers of all nodes that have an edge to the given node.
     *
     * @param nodeId identifier of the target node
     * @return the identifiers of all originating nodes
     * @throws InvalidNodeException if nodeId is invalid
     */
    Set<String> getIncomingNeighbors(String nodeId) throws InvalidNodeException;

    /**
     * Get the identifiers of all nodes that the given node has an edge to.
     *
     * @param nodeId identifier of the originating node
     * @return the identifiers of all target nodes
     * @throws InvalidNodeException if nodeId is invalid
     */
    Set<String> getOutgoingNeighbors(String nodeId) throws InvalidNodeException;
}
