package de.tukl.programmierpraktikum2021.p2.a1;

import de.tukl.programmierpraktikum2021.p1.a1.DuplicateEdgeException;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p1.a1.GraphImpl;

import java.util.*;

public class GraphExtendedImpl<D> extends GraphImpl<D> implements GraphExtended<D>{

    @Override
    public boolean hasCycle() throws InvalidNodeException {

        boolean cycle = false;
        //Liste anlegen um jeden Knoten einem Index zuzuordnen
        Set<String> nodeset =  getNodeIds();
        ArrayList<String> nodes = new ArrayList<>();
        for(String str:nodeset) nodes.add(str);

        //Tiefensuche startet von jedem einzelnen Knoten, falls der Graph nicht zusammenhängt
        for(String str:nodes){
            int[] detected = new int[nodes.size()];
            int[] finished = new int[nodes.size()];
            int detect = 1;
            int counter = 1;
            boolean[] visited = new boolean[nodes.size()];
            Stack<String> stack = new Stack<>();
            stack.push(str);
            //Stack am Anfang mit Wurzelelement
            while(!stack.empty()){
                visited[nodes.indexOf(stack.peek())] = true;
                //Besuchszeitpunkt hinterlegen wenn unentdeckt
                if(detected[nodes.indexOf(stack.peek())] == 0){
                    detected[nodes.indexOf(stack.peek())] = detect;
                    detect = detect + 1;
                }

                String topelement = stack.peek();

                //unbesuchten Nachbarn, falls vorhanden, auf Stapel legen
                for(String s: getOutgoingNeighbors(stack.peek())){
                    if(!visited[nodes.indexOf(s)]){
                        stack.push(s);
                        break;
                    }
                }
                //wenn nichts auf den Stapel gelegt wurde, oberstes Element entfernen
                if (topelement.equals(stack.peek())){
                    finished[nodes.indexOf(stack.peek())] = counter;
                    counter = counter + 1;
                    stack.pop();
                }
            }

            //Nun für jede Kante überprüfen ob sie im Baum eine Rückwärskante darstellt
            for(String s:nodes){
                if(cycle) break;
                for(String s2: getOutgoingNeighbors(s)){
                    if(!(detected[nodes.indexOf(s)] < detected[nodes.indexOf(s2)]) &&
                            !(finished[nodes.indexOf(s2)] < finished[nodes.indexOf(s)]) &&
                            detected[nodes.indexOf(s)] != 0){
                        cycle = true;
                        break;
                    }
                }
            }
        }
        return cycle;
    }



    @Override
    public boolean isConnected() throws InvalidNodeException {

        //Arraylist mit allen Knoten für Indexierung
        Set<String> nodeset =  getNodeIds();
        ArrayList<String> nodes = new ArrayList<>();
        nodes.addAll(nodeset);
        boolean[] visited = new boolean[nodes.size()];
        Stack<String> stack = new Stack<>();
        stack.push(nodes.get(0));

        //Zusammenhangskomponente umfasst alle Knoten k mit visited[k] == true
        while(!stack.empty()){
            visited[nodes.indexOf(stack.peek())] = true;
            String topelement = stack.peek();
            for(String s: getOutgoingNeighbors(stack.peek())){
                if(!visited[nodes.indexOf(s)]){
                    stack.push(s);
                    break;
                }
            }
            //Auch Incoming Neighbors, somit tiefensuche auf 'ungerichtetem' Graph
            for(String s2: getIncomingNeighbors(stack.peek())){
                if(!visited[nodes.indexOf(s2)]){
                    stack.push(s2);
                    break;
                }
            }
            if (topelement.equals(stack.peek())) stack.pop();
        }

        //Wenn nicht alle Knoten in einer Zusammenhangskomponente liegen, ist der Graph nicht zusammenhängend
        for(boolean bo: visited){
            if(!bo) return false;
        }
        return true;
    }

    @Override
    public List<String> breadthFirstSearch(String fromId, String toId) throws InvalidNodeException {
        Set<String> nodeset =  getNodeIds();

        if(!nodeset.contains(fromId)){
            throw new InvalidNodeException(fromId);
        } else if(!nodeset.contains(toId)){
            throw new InvalidNodeException(toId);
        }

        ArrayList<String> nodes = new ArrayList<>();
        for(String str:nodeset) nodes.add(str);
        boolean[] visited = new boolean[nodes.size()];
        int[] length = new int[nodes.size()];
        int counter = 1;
        String[] parent = new String[nodes.size()];
        LinkedList<String> queue = new LinkedList<String>();
        queue.add(fromId);

        while(!queue.isEmpty()){
            counter = length[nodes.indexOf(queue.peek())] + 1;
            visited[nodes.indexOf(queue.peek())] = true;

            for(String s: getOutgoingNeighbors(queue.peek())){
                if(length[nodes.indexOf(s)] == 0 || length[nodes.indexOf(s)] > counter){
                    length[nodes.indexOf(s)] = counter;
                    parent[nodes.indexOf(s)] = queue.peek();
                }
                if(!visited[nodes.indexOf(s)]){
                    queue.add(s);
                }
            }
            queue.poll();
        }

        if(visited[nodes.indexOf(toId)]){
            ArrayList<String> path = new ArrayList<String>();
            String current = toId;
            path.add(current);
            while(current != fromId){
                path.add(0,parent[nodes.indexOf(current)]);
                current = parent[nodes.indexOf(current)];
            }
            return path;
        } else return new ArrayList<>();

    }

}

