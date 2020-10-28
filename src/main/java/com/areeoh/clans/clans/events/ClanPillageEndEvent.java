package com.areeoh.clans.clans.events;

import com.areeoh.clans.clans.Clan;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanPillageEndEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Clan pillager;
    private final Clan pillagee;

    public ClanPillageEndEvent(Clan pillager, Clan pillagee) {
        this.pillager = pillager;
        this.pillagee = pillagee;
    }

    public Clan getPillagee() {
        return pillagee;
    }

    public Clan getPillager() {
        return pillager;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}