package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DisableBoneMeal extends GameModule implements Listener {
    public DisableBoneMeal(GameManager manager) {
        super(manager, "DisableBoneMeal");
    }

    @EventHandler
    public void onBoneMeal(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (player.getItemInHand().getType() == Material.INK_SACK)
                && (player.getItemInHand().getData().getData() == 15)) {
            UtilMessage.message(player, "Game", ChatColor.YELLOW + "Bone Meal" + ChatColor.GRAY + " has been disabled.");
            event.setCancelled(true);
        }
    }
}
