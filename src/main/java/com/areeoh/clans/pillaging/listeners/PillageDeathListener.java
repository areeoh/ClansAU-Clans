package com.areeoh.clans.pillaging.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.Module;
import com.areeoh.clans.pillaging.PillageManager;
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
            getManager().giveDomPoint(killerClan, deadClan, 1);
        }
    }
}