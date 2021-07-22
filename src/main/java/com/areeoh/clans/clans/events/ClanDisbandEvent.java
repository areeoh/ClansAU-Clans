package com.areeoh.clans.clans.events;

import com.areeoh.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanDisbandEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Clan clan;
    private final DisbandReason disbandReason;

    public ClanDisbandEvent(Player player, Clan clan, DisbandReason disbandReason) {
        super(player);
        this.clan = clan;
        this.disbandReason = disbandReason;
    }

    public DisbandReason getDisbandReason() {
        return disbandReason;
    }

    public Clan getClan() {
        return clan;
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

    public enum DisbandReason {
        ENERGY,
        PLAYER,
        ADMIN;
    }
}