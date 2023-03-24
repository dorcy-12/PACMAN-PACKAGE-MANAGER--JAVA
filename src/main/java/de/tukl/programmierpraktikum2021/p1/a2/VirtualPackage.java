package de.tukl.programmierpraktikum2021.p1.a2;

public class VirtualPackage extends NormalPackage {
    private final String virtualSource;

    public VirtualPackage(String name, String version, String virtualSource) {
        super(name, version);
        this.virtualSource = virtualSource;
    }

    @Override
    public String toString() {
        return this.getVirtualSource() + " provides " + super.toString();
    }

    public String getVirtualSource() {
        return virtualSource;
    }
}