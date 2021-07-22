package com.areeoh.clans.map.data;

public class MapPixel {

    protected byte color;
    private short averageY;

    public MapPixel(byte color, short averageY) {
        this.color = color;
        this.averageY = averageY;
    }

    public short getAverageY() {
        return averageY;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public void setAverageY(short averageY) {
        this.averageY = averageY;
    }
}