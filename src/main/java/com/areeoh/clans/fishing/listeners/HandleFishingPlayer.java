package com.areeoh.clans.fishing.listeners;

import com.areeoh.spigot.framework.Module;
import com.areeoh.clans.fishing.FishingManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class HandleFishingPlayer extends Module<FishingManager> implements Listener {
    public HandleFishingPlayer(FishingManager manager) {
        super(manager, "HandleFishingPlayer");
    }

    @EventHandler
    public void onFishingEvent(PlayerFishEvent event) {
        final Entity caught = event.getCaught();
        if (caught == null) {
            return;
        }
        if (caught.getType() != EntityType.PLAYER) {
            return;
        }
        caught.setVelocity(caught.getLocation().toVector().subtract(event.getPlayer().getLocation().toVector()).multiply(-1.0D).normalize());
    }
}