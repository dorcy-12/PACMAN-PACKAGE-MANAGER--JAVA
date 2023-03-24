package de.tukl.programmierpraktikum2021.p1.a2;

public class NormalPackage implements Package {
    private final String name;
    private final String version;

    @Override
    public String toString() {
        return getName() + "-" + getVersion();
    }

    public NormalPackage(String name, String version) {
        this.name = name;
        this.version = version;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }
}
