package com.areeoh.clans.listeners;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.combat.events.CustomDamageEvent;
import com.areeoh.framework.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ClanSafeZoneDamageListener extends Module<ClanManager> implements Listener {

    public ClanSafeZoneDamageListener(ClanManager manager) {
        super(manager, "ClanSafeZoneDamageListener");
    }

    @EventHandler
    public void onSafeZoneDamage(CustomDamageEvent event) {
        final Player player = event.getDamageePlayer();
        if (player == null) {
            return;
        }
        final Clan clan = getManager().getClan(player.getLocation());
        if(clan == null) {
            return;
        }
        if (clan.isAdmin() && clan.isSafe() && (event.getDamageCause() == EntityDamageEvent.DamageCause.DROWNING || event.getDamageCause() == EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
        }
    }
}
