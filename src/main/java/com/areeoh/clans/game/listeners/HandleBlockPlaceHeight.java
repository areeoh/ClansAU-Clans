package com.areeoh.clans.game.listeners;

import com.areeoh.core.utility.UtilMessage;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HandleBlockPlaceHeight extends GameModule implements Listener {

    public HandleBlockPlaceHeight(GameManager manager) {
        super(manager, "HandleBlockPlaceHeight");
    }

    @EventHandler
    public void onCancelBlock(BlockPlaceEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getBlock().getLocation().getY() > 150) {
            UtilMessage.message(event.getPlayer(), "Restriction", "You can only place blocks lower than 150Y!");
            event.setCancelled(true);
        }
    }
}