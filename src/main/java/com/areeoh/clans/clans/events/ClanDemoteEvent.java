package com.areeoh.clans.clans.events;

import com.areeoh.clans.clans.Clan;
import com.areeoh.spigot.core.client.OfflineClient;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanDemoteEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Clan clan;
    private final OfflineClient target;

    public ClanDemoteEvent(Player player, OfflineClient target, Clan clan) {
        super(player);
        this.target = target;
        this.clan = clan;
    }

    public OfflineClient getTarget() {
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