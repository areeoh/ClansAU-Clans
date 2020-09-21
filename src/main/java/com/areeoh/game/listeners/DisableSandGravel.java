package com.areeoh.game.listeners;

import com.areeoh.utility.UtilFormat;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableSandGravel extends GameModule implements Listener {
    public DisableSandGravel(GameManager manager) {
        super(manager, "DisableSandGravel");
    }

    @EventHandler
    public void onDisableSandGravel(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.SAND || event.getBlock().getType() == Material.GRAVEL) {
            event.setCancelled(true);
            UtilMessage.message(event.getPlayer(), "Game", "You cannot place " + ChatColor.YELLOW + UtilFormat.cleanString(event.getBlock().getType().name()) + ChatColor.GRAY + ".");
        }
    }
}
