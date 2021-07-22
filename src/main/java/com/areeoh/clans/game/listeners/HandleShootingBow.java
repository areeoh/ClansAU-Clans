package com.areeoh.clans.game.listeners;

import com.areeoh.champions.role.roles.Assassin;
import com.areeoh.champions.role.roles.Ranger;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class HandleShootingBow extends GameModule implements Listener {
    public HandleShootingBow(GameManager manager) {
        super(manager, "HandleShootingBow");
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (getManager(ClanManager.class).getClan(player.getLocation()) != null) {
                if (getManager(ClanManager.class).getClan(player.getLocation()).isAdmin()) {
                    if (getManager(ClanManager.class).getClan(player.getLocation()).isSafe(player.getLocation())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            if ((player.getLocation().getBlock().getType() == Material.WATER) || (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
                UtilMessage.message(player, "Skill", "You cannot shoot bows in water!");
                event.setCancelled(true);
                return;
            }
            if (getManager(Assassin.class).hasRole(player) || getManager(Ranger.class).hasRole(player)) {
                //TODO HANDLE DURABILITY
                return;
            }
            UtilMessage.message(player, "Restriction", "Only " + ChatColor.GREEN + "Rangers" + ChatColor.GRAY + " and " + ChatColor.GREEN + "Assassins" + ChatColor.GRAY + " can use bows!");
            event.setCancelled(true);
        }
    }
}
