package com.areeoh.clans.game.listeners;

import com.areeoh.core.utility.UtilMessage;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisablePlacingSkyChest extends GameModule implements Listener {
    public DisablePlacingSkyChest(GameManager manager) {
        super(manager, "DisablePlaceSkyChest");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (((block.getType() == Material.CHEST) || (block.getType() == Material.TRAPPED_CHEST) || (block.getType() == Material.FURNACE) || (block.getType() == Material.DROPPER) || (block.getType() == Material.HOPPER) || (block.getType() == Material.DISPENSER) || (block.getType() == Material.HOPPER_MINECART) || (block.getType() == Material.STORAGE_MINECART)) && (block.getLocation().getY() > 100.0D)) {
            UtilMessage.message(player, "Restriction", "You can only place chests lower than 100Y!");
            event.setCancelled(true);
        }
    }
}