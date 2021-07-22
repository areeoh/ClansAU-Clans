package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.combat.events.CustomDamageEvent;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SpongeFall extends GameModule implements Listener {

    public SpongeFall(GameManager manager) {
        super(manager, "SpongeFall");
    }

    @EventHandler
    public void onSpongeFall(CustomDamageEvent event) {
        Player player = event.getDamageePlayer();
        if (player != null) {
            if ((event.getDamageCause() == EntityDamageEvent.DamageCause.FALL) && (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE)) {
                event.setCancelled(true);
            }
        }
    }
}
