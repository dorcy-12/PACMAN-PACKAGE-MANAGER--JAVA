package de.tukl.programmierpraktikum2021.p1.a2;

import de.tukl.programmierpraktikum2021.p1.a1.DuplicateEdgeException;
import de.tukl.programmierpraktikum2021.p1.a1.GraphImpl;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p2.a1.GraphExtendedImpl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacmanImpl implements Pacman{
    public GraphExtendedImpl<Package> graph = new GraphExtendedImpl();
    public HashMap<String, Boolean> installed = new HashMap<>();
    public String path;

    public PacmanImpl(String path){
        this.path = path;
    }

    @Override
    public void buildDependencyGraph() throws IOException, InvalidNodeException, DuplicateEdgeException {
        Util util = new Util(path);
        Set<String> normal = util.getAllNormalPackages();
        for(String z: normal){
            List<String> virtuals = util.getVirtual(z);
            graph.addNode(z, new NormalPackage(z, util.getVersion(z)));
            if(virtuals != null){
                for(String st: virtuals) {
                    List<String> ohnev = new ArrayList<>(Arrays.asList(st.split("<=|=|<|>")));
                    if(ohnev.size() > 1){
                        graph.addNode(ohnev.get(0), new VirtualPackage(ohnev.get(0), ohnev.get(1), z));
                    } else{
                        graph.addNode(ohnev.get(0), new VirtualPackage(ohnev.get(0), util.getVersion(st), z));
                    }
                    try{
                        graph.addEdge(ohnev.get(0), z);
                    } catch (DuplicateEdgeException e){

                    }
                }
            }
        }
        for(String s: normal){
            List<String> deps = util.getDependencies(s);
            if(deps != null){
                for(String t: deps){
                    List<String> ohnev = new ArrayList<>(Arrays.asList(t.split("=|<=|<|>")));
                    graph.addEdge(s, ohnev.get(0));
                }
            }
        }
    }

    @Override
    public Set<String> whoRequires(String pkg) throws InvalidNodeException, IOException {
        Util util = new Util(path);
        return graph.getIncomingNeighbors(pkg).stream().filter(x->util.getAllNormalPackages().contains(x)).collect(Collectors.toSet());
    }

    @Override
    public String transitiveDependencies(String pkg) throws InvalidNodeException, IOException {
        Util util = new Util(path);
        String s = pkg + "-" +util.getVersion(pkg) + "\n";
        s = s + getTransitives(pkg, 0);
        return s;
    }

    public String getTransitives(String n, int level) throws InvalidNodeException {
        Object[] deps = (graph.getOutgoingNeighbors(n)).toArray();
        ArrayList<String> d = new ArrayList<>();
        for(Object o:deps)d.add((String)o);

        String s = "";
        if(d.size() > 0){
            for(int i = 0; i < d.size(); i++){
                if(i != d.size() -1){
                    s = s + blanks(level) + "├--" + graph.getData(d.get(i)).toString() +"\n";
                } else {
                    s = s + blanks(level) + "└--" + graph.getData(d.get(i)).toString() +"\n";
                }
                s = s + getTransitives(d.get(i), level+1);
            }
        }
        return s;
    }

    public String blanks(int i){
        String b = "";
        i = i*4;
        while(i>0){
            b = b + " ";
            i--;
        }
        return b;
    }

    public ArrayList<Package> getTransitives2(String pkg) throws InvalidNodeException {
        Object[] deps = (graph.getOutgoingNeighbors(pkg)).toArray();
        ArrayList<Package> ret = new ArrayList<>();

        for(Object o:deps){
            if(!installed.containsKey((String)o)){
                ret.add(graph.getData((String) o));
                for(Package pack:getTransitives2((String) o)) ret.add(pack);
            }
        }
        return ret;
    }

    @Override
    public List<Package> buildInstallList(String pkg) throws InvalidNodeException {
        ArrayList<Package> il = getTransitives2(pkg);
        ArrayList<Package> ret = new ArrayList<>();
        for(int i = il.size()-1; i > -1; i--){
            if(!ret.contains(il.get(i))){
                ret.add(il.get(i));
            }
        }
        if(!installed.containsKey(pkg)) ret.add(graph.getData(pkg));
        return ret;
    }

    @Override
    public void install(String pkg) throws InvalidNodeException, IOException {
        List<Package> tobeinstalled = buildInstallList(pkg);
        installed.put(tobeinstalled.get(tobeinstalled.size()-1).getName(), true);
        tobeinstalled.remove(tobeinstalled.size()-1);
        for(Package pack:tobeinstalled){
            installed.put(pack.getName(), false);
        }
    }

    public void tester() throws IOException {
        Util util = new Util(path);
        for(String s:util.getAllConflictingPackages()){
            System.out.println(s);
        }
    }

}

