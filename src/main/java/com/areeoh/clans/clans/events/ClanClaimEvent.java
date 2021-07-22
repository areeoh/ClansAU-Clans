package com.areeoh.clans.clans.events;

import com.areeoh.clans.clans.Clan;
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
    private boolean outline;
    private boolean message;

    public ClanClaimEvent(Player player, Chunk chunk, Clan clan) {
        super(player);
        this.chunk = chunk;
        this.clan = clan;
        this.outline = true;
        this.message = true;
    }

    public ClanClaimEvent(Player who, Chunk chunk, Clan clan, boolean outline, boolean message) {
        super(who);
        this.chunk = chunk;
        this.clan = clan;
        this.outline = outline;
        this.message = message;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
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
