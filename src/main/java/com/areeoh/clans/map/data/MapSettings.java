package com.areeoh.clans.map.data;

public class MapSettings {

    private boolean update = false;
    private Scale scale = Scale.CLOSEST;
    private int mapX, mapZ;
    protected long lastUpdated;

    public MapSettings(int lastX, int lastZ) {
        this.lastUpdated = System.currentTimeMillis();
        this.mapX = lastX;
        this.mapZ = lastZ;
    }

    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    public int getMapZ() {
        return mapZ;
    }

    public void setMapZ(int mapZ) {
        this.mapZ = mapZ;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setUpdate(boolean b) {
        this.update = b;
    }

    public boolean isUpdate() {
        return update;
    }

    public Scale getScale() {
        return scale;
    }

    public Scale setScale(Scale scale) {
        this.scale = scale;
        return this.scale;
    }

    public enum Scale {
        CLOSEST(0),
        CLOSE(1),
        NORMAL(2),
        FAR(3),
        FARTHEST(4);

        private final byte value;

        Scale(int value) {
            this.value = (byte)value;
        }

        public byte getValue() {
            return this.value;
        }
    }
}
