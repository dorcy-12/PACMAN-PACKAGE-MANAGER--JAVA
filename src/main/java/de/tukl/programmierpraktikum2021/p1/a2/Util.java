package de.tukl.programmierpraktikum2021.p1.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {
    private final Map<String, String> pkgVersion = new HashMap<>();
    private final Map<String, List<String>> pkgDependencies = new HashMap<>();
    private final Map<String, List<String>> pkgVirtual = new HashMap<>();
    private final Map<String, List<String>> pkgConflicts = new HashMap<>();


    // Get names of all normal (i.e. non virtual) packages in the package repository.
    public Set<String> getAllNormalPackages() {
        return Collections.unmodifiableSet(pkgVersion.keySet());
    }

    // Get names of all normal packages in the package repository that provide virtual packages.
    public Set<String> getAllVirtualPackages() {
        return Collections.unmodifiableSet(pkgVirtual.keySet());
    }

    // Get names of all packages that have conflicts with other packages
    public Set<String> getAllConflictingPackages() {
        return Collections.unmodifiableSet(pkgConflicts.keySet());
    }

    // get version of a package pkg
    public String getVersion(String pkg) {
        return pkgVersion.get(pkg);
    }

    // get list of dependencies for package pkg
    public List<String> getDependencies(String pkg) {
        return pkgDependencies.get(pkg);
    }

    // get names of all virtual packages that the package pkg provides
    public List<String> getVirtual(String pkg) {
        return pkgVirtual.get(pkg);
    }

    // get names of packages that are in conflict with pkg
    public List<String> getConflicts(String pkg) {
        return pkgConflicts.get(pkg);
    }

    private void parsePacmanDesc(String content) {
        String[] parts = content.split("(\\r\\n|\\n|\\r){2}");
        Map<String, List<String>> m = new HashMap<>();
        for (String p : parts) {
            List<String> partLines = new ArrayList<>(Arrays.asList(p.split("(\\r\\n|\\n|\\r)")));
            String key = partLines.remove(0);
            m.put(key.replace("%", ""), partLines);
        }
        pkgVersion.put(m.get("NAME").get(0), m.get("VERSION").get(0));
        if (m.get("DEPENDS") != null && !m.get("DEPENDS").isEmpty())
            pkgDependencies.put(m.get("NAME").get(0), m.get("DEPENDS"));
        if (m.get("PROVIDES") != null && !m.get("PROVIDES").isEmpty())
            pkgVirtual.put(m.get("NAME").get(0), m.get("PROVIDES"));
        if (m.get("CONFLICTS") != null && !m.get("CONFLICTS").isEmpty()) {
            pkgConflicts.put(m.get("NAME").get(0), m.get("CONFLICTS"));
            for (String c : m.get("CONFLICTS")) {
                if (pkgVersion.containsKey(c)) {
                    if (pkgConflicts.get(c) == null)
                        pkgConflicts.put(c, m.get("NAME"));
                    else {
                        Set<String> z = new HashSet<>(pkgConflicts.get(c));
                        z.addAll(m.get("NAME"));
                        pkgConflicts.put(c, new ArrayList<>(z));
                    }
                }
            }
        }
    }

    public Util(String filename) throws IOException {
        ZipFile zip = new ZipFile(filename);
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                InputStream s = zip.getInputStream(entry);
                String result = new BufferedReader(new InputStreamReader(s)).lines().collect(Collectors.joining("\n"));
                parsePacmanDesc(result);
            }
        }
        zip.close();
    }

}
