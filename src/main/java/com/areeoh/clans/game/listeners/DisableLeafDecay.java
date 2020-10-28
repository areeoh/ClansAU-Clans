package com.areeoh.clans.game.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class DisableLeafDecay extends GameModule implements Listener {

    public DisableLeafDecay(GameManager manager) {
        super(manager, "DisableLeafDecay");
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        final Clan clan = getManager(ClanManager.class).getClan(event.getBlock().getLocation().getChunk());
        if (clan == null) {
            return;
        }
        if (!clan.isAdmin()) {
            return;
        }
        event.setCancelled(true);
    }
}
