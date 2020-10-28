package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class DisableBeacon extends GameModule implements Listener {
    public DisableBeacon(GameManager manager) {
        super(manager, "DisableBeacon");
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (event.isCancelled()) {
                return;
            }
            if (event.getInventory().getType() == InventoryType.BEACON) {
                event.setCancelled(true);
            }
        }
    }
}
