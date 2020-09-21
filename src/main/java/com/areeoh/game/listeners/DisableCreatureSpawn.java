package com.areeoh.game.listeners;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class DisableCreatureSpawn extends GameModule implements Listener {

    public DisableCreatureSpawn(GameManager manager) {
        super(manager, "DisableCreatureSpawn");
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Clan clan = getManager(ClanManager.class).getClan(event.getEntity().getLocation().getChunk());
        if ((clan != null && clan.isAdmin())) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                event.setCancelled(true);
            }
        }
    }
}