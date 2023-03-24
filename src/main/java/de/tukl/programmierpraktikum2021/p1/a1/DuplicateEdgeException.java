package de.tukl.programmierpraktikum2021.p1.a1;

public class DuplicateEdgeException extends GraphException {
    public final String fromId;
    public final String toId;

    public DuplicateEdgeException(String fromId, String toId) {
        super("The edge " + fromId + " -> " + toId + " already exists!");
        this.fromId = fromId;
        this.toId = toId;
    }
}
