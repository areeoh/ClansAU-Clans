package com.areeoh.game.listeners;

import com.areeoh.utility.UtilFormat;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableBedrock extends GameModule implements Listener {

    public DisableBedrock(GameManager manager) {
        super(manager, "DisableBedrock");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        if (block.getType() == Material.BEDROCK) {
            UtilMessage.message(player, "Game", "You cannot place " + ChatColor.YELLOW + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".");
            event.setCancelled(true);
        }
    }
}