package com.areeoh.blockregen.data;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockRegenData {

    private final Material oldMaterial;
    private final Material newMaterial;
    private final byte oldMaterialID;
    private final byte newMaterialID;
    private final long sysTime;
    private long timeLength;

    public BlockRegenData(Block block, Material newMaterial, byte newMaterialID, long timeLength) {
        this.oldMaterial = block.getType();
        this.oldMaterialID = block.getData();
        this.newMaterial = newMaterial;
        this.newMaterialID = newMaterialID;
        this.sysTime = System.currentTimeMillis();
        this.timeLength = timeLength;
        block.setTypeIdAndData(newMaterial.getId(), newMaterialID, true);
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public Material getOldMaterial() {
        return oldMaterial;
    }

    public Material getNewMaterial() {
        return newMaterial;
    }

    public byte getOldMaterialID() {
        return oldMaterialID;
    }

    public byte getNewMaterialID() {
        return newMaterialID;
    }
}