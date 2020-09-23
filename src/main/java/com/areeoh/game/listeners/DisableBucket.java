package com.areeoh.game.listeners;

import com.areeoh.client.ClientManager;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class DisableBucket extends GameModule implements Listener {
    public DisableBucket(GameManager manager) {
        super(manager, "DisableBucket");
    }

    @EventHandler
    public void handleBucket(PlayerBucketEmptyEvent event) {
        if (getManager(ClientManager.class).getClient(event.getPlayer().getUniqueId()).isAdministrating()) {
            return;
        }
        event.setCancelled(true);
        UtilMessage.message(event.getPlayer(), "Game", "Your " + ChatColor.YELLOW + "Bucket" + ChatColor.GRAY + " broke!");
        event.getPlayer().setItemInHand(new ItemStack(Material.IRON_INGOT, event.getPlayer().getItemInHand().getAmount() * 3));
        event.getBlockClicked().getState().update();
    }

    @EventHandler
    public void handleBucket(PlayerBucketFillEvent event) {
        if (getManager(ClientManager.class).getClient(event.getPlayer().getUniqueId()).isAdministrating()) {
            return;
        }
        event.setCancelled(true);
        UtilMessage.message(event.getPlayer(), "Game", "Your " + ChatColor.YELLOW + "Bucket" + ChatColor.GRAY + " broke!");
        event.getPlayer().setItemInHand(new ItemStack(Material.IRON_INGOT, event.getPlayer().getItemInHand().getAmount() * 3));
        event.getBlockClicked().getState().update();
    }
}