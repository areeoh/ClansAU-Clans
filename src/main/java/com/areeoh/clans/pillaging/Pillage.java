package com.areeoh.clans.pillaging;

import com.areeoh.clans.clans.Clan;

public class Pillage {

    private final Clan pillager, pillagee;
    private final long start = System.currentTimeMillis();
    private long lastAnnounce = System.currentTimeMillis();
    private final long length;

    public Pillage(Clan pillager, Clan pillagee, long length) {
        this.pillager = pillager;
        this.pillagee = pillagee;
        this.length = length;
    }

    public Clan getPillager() {
        return pillager;
    }

    public Clan getPillagee() {
        return pillagee;
    }

    public long getStart() {
        return start;
    }

    public long getLastAnnounce() { return lastAnnounce; }

    public void setLastAnnounce(long lastAnnounce) { this.lastAnnounce = lastAnnounce; }

    public long getTimeRemaining() {
        return getStart() + getLength() - System.currentTimeMillis();
    }

    public long getLength() {
        return length;
    }
}
