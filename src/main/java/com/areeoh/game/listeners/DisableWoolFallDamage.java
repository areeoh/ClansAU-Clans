package com.areeoh.game.listeners;

import com.areeoh.combat.events.CustomDamageEvent;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DisableWoolFallDamage extends GameModule implements Listener {

    public DisableWoolFallDamage(GameManager manager) {
        super(manager, "DisableWoolFallDamage");
    }

    @EventHandler
    public void onWoolFall(CustomDamageEvent event) {
        if ((event.getDamageePlayer() != null) && (event.getDamageCause() == EntityDamageEvent.DamageCause.FALL)) {
            final Player player = (Player) event.getDamagee();
            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOL || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.CARPET) {
                event.setCancelled(true);
            }
        }
    }

}
