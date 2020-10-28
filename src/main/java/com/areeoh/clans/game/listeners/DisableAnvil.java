package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.core.utility.UtilFormat;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DisableAnvil extends GameModule implements Listener {

    public DisableAnvil(GameManager manager) {
        super(manager, "DisableAnvil");
    }

    @EventHandler
    public void AnvilDisable(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.ANVIL) {
            return;
        }
        UtilMessage.message(event.getPlayer(), "Game",ChatColor.YELLOW + UtilFormat.cleanString(event.getClickedBlock().getType().toString()) + ChatColor.GRAY + " is disabled.");
        event.setCancelled(true);
    }
}
