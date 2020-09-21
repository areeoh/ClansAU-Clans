package com.areeoh.map.data;

public class Coords {

    private final int x,z;
    private final String world;

    public Coords(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }
}