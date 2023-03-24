package de.tukl.programmierpraktikum2021.P2;

import de.tukl.programmierpraktikum2021.p1.a1.DuplicateEdgeException;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p1.a2.Package;
import de.tukl.programmierpraktikum2021.p2.a2.PacmanExtendedImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PacmanExtendedTest {

    @Test
    public void PacmanExtendendImpl() throws InvalidNodeException, DuplicateEdgeException, IOException {
        PacmanExtendedImpl Pacm = new PacmanExtendedImpl("./src/main/resources/core-cycle.db.zip");
        Pacm.buildDependencyGraph();

        // install cycle
        assertTrue(Pacm.isCycle());
        assertDoesNotThrow(()->Pacm.install("gcc-libs")); // ein Packet in einem Zyklus soll installiert werden ohne Fehlermeldung.
        for(Package o : Pacm.buildInstallList("gcc-libs")){
               assertTrue(Pacm.installed.containsKey(o.getName()));
        }

        //install
        Pacm.install("iptables");
        assertTrue(Pacm.installed.containsKey("iptables"));

        Pacm.install("iptables-nft");//iptables-nft steht in Konflikt mit dem bereits installierten Packet iptables
        assertFalse(Pacm.installed.containsKey("iptables-nft"));

        Pacm.install("openresolv");
        assertTrue(Pacm.installed.containsKey("openresolv"));

        Pacm.install("systemd-resolvconf");//systemd-resolvconf steht in Konflikt mit dem bereits installierten Packet openresolv
        assertFalse(Pacm.installed.containsKey("systemd-resolvconf"));

        assertArrayEquals(new String[]{"iptables", "openresolv","gcc-libs"}, Pacm.getInstalledExplicitly().toArray(new String[0]));

        assertThrows(InvalidNodeException.class, () -> Pacm.install("x"));
        assertThrows(InvalidNodeException.class, () -> Pacm.install("Z"));

        //remove
        Pacm.remove("iptables");
        assertFalse(Pacm.installed.containsKey("iptables")); // iptable hängt von nichts in installed
        assertFalse(Pacm.getInstalledExplicitly().contains("iptables"));
        
        Pacm.remove("bash"); //Openresolv höngt von bash ab.
        assertTrue(Pacm.installed.containsKey("bash")); //bash bleibt in installed

        assertThrows(InvalidNodeException.class, () -> Pacm.remove("X"));
        assertThrows(InvalidNodeException.class, () -> Pacm.remove("Y"));

        //transitive dependencies

        assertDoesNotThrow(()->Pacm.transitiveDependencies("gcc-libs"));//Transitivedependencies von gcc-libs ohne Fehlermeldung

        String ss = Pacm.transitiveDependencies("gcc-libs").replaceAll("\\s+","");
        List<String> translist = Arrays.stream(ss.split("├--|└--")).collect(Collectors.toList());

        assertTrue(translist.containsAll(Pacm.buildInstallList("gcc-libs").
                    stream().map(Package::getName).collect(Collectors.toList())));//sicherstellen das alle zu installierenden Pakete in transitivedepencies enthalten sind.

        String bb = Pacm.transitiveDependencies("libaio").replaceAll("\\s+","");
        List<String>  liblist =Arrays.stream(bb.split("├--|└--")).collect(Collectors.toList()) ;

        assertEquals(1, liblist.size());
        assertEquals("libaio-0.3.112-2", liblist.get(0));

        System.out.println(Pacm.transitiveDependencies("gcc-libs"));

        assertThrows(InvalidNodeException.class, ()-> Pacm.transitiveDependencies("sqkite"));
        assertThrows(InvalidNodeException.class, ()-> Pacm.transitiveDependencies("X"));


    }

}

