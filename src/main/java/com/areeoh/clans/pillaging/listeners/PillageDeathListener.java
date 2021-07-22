package com.areeoh.clans.pillaging.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.framework.Module;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.spigot.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PillageDeathListener extends Module<PillageManager> implements Listener {

    public PillageDeathListener(PillageManager manager) {
        super(manager, "PillageDeathListener");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        final Player player = event.getEntity();
        final Clan deadClan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (deadClan == null) {
            return;
        }
        final Player killer = player.getKiller();
        if (killer == null) {
            return;
        }
        final Clan killerClan = getManager(ClanManager.class).getClan(killer);
        if (killerClan != null && getManager(ClanManager.class).getClanRelation(deadClan, killerClan) == ClanManager.ClanRelation.ENEMY) {
            if(UtilTime.elapsed(deadClan.getEnemyCooldown(), 1800000)) {
                killerClan.inform(true, "Clans", "Your Clan did not gain a Dominance Point because " + ChatColor.RED + "Clan " + deadClan.getName() + ChatColor.GRAY + " has been pillaged recently.");
                return;
            }
            getManager().giveDomPoint(killerClan, deadClan, 1);
        }
    }
}