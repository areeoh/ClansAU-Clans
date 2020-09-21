package com.areeoh.clans.events;

import com.areeoh.clans.Clan;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanClaimEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final Chunk chunk;
    private final Clan clan;

    public ClanClaimEvent(Player player, Clan clan) {
        super(player);
        this.chunk = player.getLocation().getChunk();
        this.clan = clan;
    }

    public Chunk getChunk() {
        return chunk;
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
