package com.areeoh.clans.clans.events;

import com.areeoh.clans.clans.Clan;
import com.areeoh.core.client.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanKickEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Client target;
    private final Clan clan;

    public ClanKickEvent(Player player, Client target, Clan clan) {
        super(player);
        this.target = target;
        this.clan = clan;
    }

    public Client getTarget() {
        return target;
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
}