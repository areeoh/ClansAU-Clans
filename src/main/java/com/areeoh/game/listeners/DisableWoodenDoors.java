package com.areeoh.game.listeners;

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
import org.bukkit.inventory.ItemStack;

public class DisableWoodenDoors extends GameModule implements Listener {

    public DisableWoodenDoors(GameManager manager) {
        super(manager, "DisableWoodenDoors");
    }

    @EventHandler
    public void onBlockCancelPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        if ((block.getType() == Material.WOODEN_DOOR)
                || (block.getType() == Material.ACACIA_DOOR)
                || (block.getType() == Material.SPRUCE_DOOR)
                || (block.getType() == Material.BIRCH_DOOR)
                || (block.getType() == Material.JUNGLE_DOOR)
                || (block.getType() == Material.DARK_OAK_DOOR)) {
            block.setType(Material.AIR);
            block.getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.IRON_DOOR));
            UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Doors" + ChatColor.GRAY + ".");
        } else if (block.getType() == Material.TRAP_DOOR) {
            block.setType(Material.AIR);
            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_TRAPDOOR));
            UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Trap Doors" + ChatColor.GRAY + ".");
        }
    }
}