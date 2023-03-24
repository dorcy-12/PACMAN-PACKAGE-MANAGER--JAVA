package de.tukl.programmierpraktikum2021.p2.a2;

import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p1.a2.Package;
import de.tukl.programmierpraktikum2021.p1.a2.PacmanImpl;
import de.tukl.programmierpraktikum2021.p1.a2.Util;

import java.io.IOException;
import java.util.*;

public class PacmanExtendedImpl extends PacmanImpl implements PacmanExtended{

    public PacmanExtendedImpl(String path) {
        super(path);
    }

    public boolean isCycle() throws InvalidNodeException {
        return graph.hasCycle();
    }

    @Override
    public void install(String pkg) throws IOException, InvalidNodeException {
        boolean conflict = false;
        Util util = new Util(path);
        List<String> conf = util.getConflicts(pkg);
        if(conf != null){
            for(String s:conf){
                if(installed.containsKey(s)){
                    System.out.println(pkg + " steht in Konflikt mit dem Paket " + s +
                            "\n Die Installation wurde abgebrochen");
                    conflict = true;
                }
            }
        }

        if(!conflict){
            List<Package> tobeinstalled = buildInstallList(pkg);
            installed.put(tobeinstalled.get(tobeinstalled.size()-1).getName(), true);
            tobeinstalled.remove(tobeinstalled.size()-1);
            for(Package pack:tobeinstalled){
                installed.put(pack.getName(), false);
            }
        }
    }

    @Override
    public List<Package> buildInstallList(String pkg) throws InvalidNodeException {
        HashSet<String> h = new HashSet<>();
        h.add(pkg);
        ArrayList<Package> il = getTransitives2(pkg, h);
        ArrayList<Package> ret = new ArrayList<>();
        for(int i = il.size()-1; i > -1; i--){
            if(!ret.contains(il.get(i))){
                ret.add(il.get(i));
            }
        }
        if(!installed.containsKey(pkg)) ret.add(graph.getData(pkg));
        return ret;
    }

    public ArrayList<Package> getTransitives2(String pkg, Set s) throws InvalidNodeException {
        Object[] deps = (graph.getOutgoingNeighbors(pkg)).toArray();
        ArrayList<Package> ret = new ArrayList<>();
        for(Object o:deps){
            if(!installed.containsKey((String)o)){
                if(s.contains((String) o)){
                    System.out.println("Das Paket " + (String) o + " wird von " + pkg + " benötigt," +
                            " hängt aber parallel selbst von " + pkg + " ab.");
                } else {
                    ret.add(graph.getData((String) o));
                    HashSet<String> newH = new HashSet<>();
                    for(Object st:s) newH.add((String)st);
                    newH.add((String) o);
                    for(Package pack:getTransitives2((String) o, newH)) ret.add(pack);
                }
            }
        }
        return ret;
    }

    @Override
    public Set<String> getInstalled() {
        return installed.keySet();
    }

    @Override
    public Set<String> getInstalledExplicitly() {
        HashSet<String> explicit = new HashSet<String>();
        for(String s:installed.keySet()){
            if(installed.get(s)) explicit.add(s);
        }

        return explicit;
    }

    @Override
    public void remove(String pkg) throws InvalidNodeException {
        boolean canBeDeleted = true;
        Set<String> deps = graph.getIncomingNeighbors(pkg);
        ArrayList<String> installeddeps = new ArrayList<>();
        for(String s:deps){
            if(installed.containsKey(s)){
                canBeDeleted = false;
                installeddeps.add(s);
            }
        }

        if(!canBeDeleted){
            String pakete = "";
            for(String st:installeddeps) pakete = pakete + st + " ";
            if (installeddeps.size() > 1){
                System.out.println("Das Paket " + pkg + " kann nicht gelöscht werden." +
                        " Folgende Pakete hängen funktional von " + pkg + " ab: " + pakete);
            } else{
                System.out.println("Das Paket " + pkg + " kann nicht gelöscht werden." +
                        " Folgendes Paket hängt funktional von " + pkg + " ab: " + pakete);
            }
        } else {
            installed.remove(pkg);
            for(String str:graph.getOutgoingNeighbors(pkg)){
                try{
                    if(!installed.get(str)){
                        remove(str);
                    }
                } catch (NullPointerException n){
                }
            }
        }
    }

    @Override
    public String transitiveDependencies(String pkg) throws InvalidNodeException, IOException {
        Util util = new Util(path);
        String s = pkg + "-" +util.getVersion(pkg) + "\n";
        HashSet<String> h = new HashSet<>();
        h.add(pkg);
        s = s + getTransitives(pkg, 0, h);
        return s;
    }

    public String getTransitives(String n, int level, Set h) throws InvalidNodeException {
        Object[] deps = (graph.getOutgoingNeighbors(n)).toArray();
        ArrayList<String> d = new ArrayList<>();
        for(Object o:deps){
            if(!h.contains(o)){
                d.add((String)o);
            }
        }

        String s = "";
        if(d.size() > 0){
            for(int i = 0; i < d.size(); i++){
                if(i != d.size() -1){
                    s = s + blanks(level) + "├--" + graph.getData(d.get(i)).toString() +"\n";
                } else {
                    s = s + blanks(level) + "└--" + graph.getData(d.get(i)).toString() +"\n";
                }
                HashSet<String> newH = new HashSet<>();
                for(Object str:h) newH.add((String)str);
                newH.add(d.get(i));
                s = s + getTransitives(d.get(i), level+1, newH);
            }
        }
        return s;
    }
}
