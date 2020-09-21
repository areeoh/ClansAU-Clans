package com.areeoh.pillaging;

import com.areeoh.clans.Clan;

public class Pillage {

    private final Clan pillager, pillagee;
    private final long start = System.currentTimeMillis();
    private long lastAnnounce = System.currentTimeMillis();

    public Pillage(Clan pillager, Clan pillagee) {
        this.pillager = pillager;
        this.pillagee = pillagee;
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
        return getStart() + 900000L - System.currentTimeMillis();
    }
}
