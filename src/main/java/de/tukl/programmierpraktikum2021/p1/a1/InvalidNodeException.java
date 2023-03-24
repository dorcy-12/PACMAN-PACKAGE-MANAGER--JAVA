package de.tukl.programmierpraktikum2021.p1.a1;

public class InvalidNodeException extends GraphException {
    public final String nodeId;

    public InvalidNodeException(String nodeId) {
        super("There is no node " + nodeId + "!");
        this.nodeId = nodeId;
    }
}
