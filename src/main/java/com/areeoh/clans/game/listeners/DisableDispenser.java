package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.core.utility.UtilFormat;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class DisableDispenser extends GameModule implements Listener {

    public DisableDispenser(GameManager manager) {
        super(manager, "DisableDispenser");
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            final Player player = (Player) event.getPlayer();
            if (event.getInventory().getType() == InventoryType.DISPENSER) {
                UtilMessage.message(player, "Game", ChatColor.YELLOW + UtilFormat.cleanString(event.getInventory().getType().toString()) + ChatColor.GRAY + " is disabled.");
                event.setCancelled(true);
            }
        }
    }
}