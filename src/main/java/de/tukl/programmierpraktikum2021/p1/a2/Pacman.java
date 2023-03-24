package de.tukl.programmierpraktikum2021.p1.a2;

import de.tukl.programmierpraktikum2021.p1.a1.DuplicateEdgeException;
import de.tukl.programmierpraktikum2021.p1.a1.InvalidNodeException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Pacman {
    /**
     * Create the dependency graph from a package database.
     *
     * @throws IOException If a problem occurred while reading the package database.
     */
    void buildDependencyGraph() throws IOException, InvalidNodeException, DuplicateEdgeException;

    /**
     * Returns a set of packages that directly depend on package pkg
     *
     * @param pkg The name of the package.
     * @return A set of package names that depend on package pkg.
     * @throws InvalidNodeException If a package is not contained in the dependency graph
     */
    Set<String> whoRequires(String pkg) throws InvalidNodeException, IOException;

    /**
     * Returns a textual representation of the dependency tree for a package pkg.
     *
     * @param pkg The name of the package.
     * @return The textual representation of the dependency tree.
     * @throws InvalidNodeException If a package is not contained in the dependency graph
     */
    String transitiveDependencies(String pkg) throws InvalidNodeException, IOException;

    /**
     * Build a list of packages which need to be installed in order to install a package pkg.
     * Transitive dependencies have to be considered.
     * The returned list is ordered, such that packages that depend on other packages in the
     * list are located after the packages that they depend on.
     * <p>
     * Packages that are already installed should not appear in the returned list.
     *
     * @param pkg The name of the package that the list should be built for.
     * @return A sorted list with packages that need to be installed in order to install pkg.
     * @throws InvalidNodeException If a package is not contained in the dependency graph
     */
    List<Package> buildInstallList(String pkg) throws InvalidNodeException;

    /**
     * Install package pkg.
     * Uses the buildInstallList method to determine packages that need to be installed and keeps track
     * of installed packages.
     *
     * @param pkg The name of the package that should be installed.
     * @throws InvalidNodeException If a package is not contained in the dependency graph
     */
    void install(String pkg) throws InvalidNodeException, IOException;
}
