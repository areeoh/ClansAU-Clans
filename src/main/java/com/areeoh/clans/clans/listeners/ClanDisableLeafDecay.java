package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.framework.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class ClanDisableLeafDecay extends Module<ClanManager> implements Listener {

    public ClanDisableLeafDecay(ClanManager manager) {
        super(manager, "ClanDisableLeafDecay");
    }

    @EventHandler
    public void stopLeafDecay(LeavesDecayEvent event) {
        if (getManager().getClan(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }
}
