package de.tukl.programmierpraktikum2021.p2.a2;

import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;
import de.tukl.programmierpraktikum2021.p1.a2.Pacman;

import java.util.Set;

public interface PacmanExtended extends Pacman {
    /**
     * Set of all (explicitly and implicitly) installed packages.
     *
     * @return Set of all installed packages.
     */
    Set<String> getInstalled();

    /**
     * Set of explicitly installed packages.
     *
     * @return Set of explicitly installed packages.
     */
    Set<String> getInstalledExplicitly();

    /**
     * Remove package pkg.
     * Don't remove if pkg is needed by another installed package.
     * If pkg can be removed, also remove (transitive) dependencies of pkg, if they are
     * not needed by other installed packages.
     *
     * @param pkg The name of the package that should be removed.
     * @throws InvalidNodeException If a package is not contained in the dependency graph
     */
    void remove(String pkg) throws InvalidNodeException;
}
