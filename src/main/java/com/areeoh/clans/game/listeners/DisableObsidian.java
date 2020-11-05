package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.core.utility.UtilFormat;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableObsidian extends GameModule implements Listener {

    public DisableObsidian(GameManager manager) {
        super(manager, "DisableObsidian");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.OBSIDIAN) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        if (block.getType() == Material.OBSIDIAN) {
            UtilMessage.message(player, "Game", "You cannot place " + ChatColor.YELLOW + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".");
            event.setCancelled(true);
        }
    }
}