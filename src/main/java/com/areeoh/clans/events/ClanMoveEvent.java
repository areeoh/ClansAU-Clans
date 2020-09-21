package com.areeoh.clans.events;

import com.areeoh.clans.Clan;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanMoveEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Clan clanFrom, clanTo;
    private final Location locFrom, locTo;

    public ClanMoveEvent(Player player, Clan clanFrom, Clan clanTo, Location locFrom, Location locTo) {
        super(player);
        this.clanFrom = clanFrom;
        this.clanTo = clanTo;
        this.locFrom = locFrom;
        this.locTo = locTo;
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

    public Clan getClanFrom() {
        return clanFrom;
    }

    public Clan getClanTo() {
        return clanTo;
    }

    public Location getLocFrom() {
        return locFrom;
    }

    public Location getLocTo() {
        return locTo;
    }
}