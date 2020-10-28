package com.areeoh.clans.map.data;

import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

public class ChunkData {

    private byte color;
    private final String world;
    private final int x,z;
    private final String clan;
    private final Set<BlockFace> blockFaceSet;

    public ChunkData(String world, byte color, int x, int z, String clan) {
        this.world = world;
        this.color = color;
        this.x = x;
        this.z = z;
        this.clan = clan;
        this.blockFaceSet = new HashSet<>();
    }

    public String getWorld() {
        return world;
    }

    public Set<BlockFace> getBlockFaceSet() {
        return blockFaceSet;
    }

    public String getClan() {
        return clan;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getZ() { return z; }
}