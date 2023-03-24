package de.tukl.programmierpraktikum2021.p1.a1;

import java.util.Set;
import java.util.*;

public class GraphImpl<D> implements Graph<D>{

    private final HashMap<String, D> values = new HashMap<>();
    private final HashMap<String, ArrayList<String>> outgoing = new HashMap<>();
    private final HashMap<String, ArrayList<String>> incoming = new HashMap<>();

    @Override
    public void addNode(String name, D data) {
        values.put(name, data);
    }


    @Override
    public D getData(String nodeId) throws InvalidNodeException {
        D v = values.get(nodeId);
        if (v == null){
            throw new InvalidNodeException(nodeId);
        } else {
            return v;
        }
    }

    @Override
    public void setData(String nodeId, D data) throws InvalidNodeException {
        Object v = values.get(nodeId);
        if (v == null){
            throw new InvalidNodeException(nodeId);
        } else {
            values.put(nodeId, data);
        }
    }

    @Override
    public void addEdge(String fromId, String toId) throws InvalidNodeException, DuplicateEdgeException {
        if(!values.containsKey(fromId)) throw new InvalidNodeException(fromId);
        else if (!values.containsKey(toId)) throw new InvalidNodeException(toId);
        else{
            try {
                if (outgoing.get(fromId).contains(toId)) throw new DuplicateEdgeException(fromId, toId);
            } catch(NullPointerException e){}

            try {
                outgoing.get(fromId).add(toId);
            } catch (NullPointerException e){
                outgoing.put(fromId, new ArrayList<String>(Collections.singleton(toId)));
            }
            try {
                incoming.get(toId).add(fromId);
            } catch (NullPointerException e){
                incoming.put(toId, new ArrayList<String>(Collections.singleton(fromId)));
            }

        }
    }

    @Override
    public Set<String> getNodeIds() {
        return values.keySet();
    }

    @Override
    public Set<String> getIncomingNeighbors(String nodeId) throws InvalidNodeException {
        if (!values.containsKey(nodeId)) throw new InvalidNodeException(nodeId);
        else {
            HashSet<String> s = new HashSet<>();
            if (incoming.containsKey(nodeId)){
                Iterator<String> i = incoming.get(nodeId).iterator();
                while(i.hasNext()) s.add(i.next());
            }
            return s;
        }
    }

    @Override
    public Set<String> getOutgoingNeighbors(String nodeId) throws InvalidNodeException {
        if (!values.containsKey(nodeId)) throw new InvalidNodeException(nodeId);
        else {
            HashSet<String> s = new HashSet<>();
            if (outgoing.containsKey(nodeId)){
                Iterator<String> i = outgoing.get(nodeId).iterator();
                while(i.hasNext()) s.add(i.next());
            }
            return s;
        }
    }

}
