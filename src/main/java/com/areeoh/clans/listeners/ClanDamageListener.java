package com.areeoh.clans.listeners;

import com.areeoh.clans.ClanManager;
import com.areeoh.combat.events.CustomDamageEvent;
import com.areeoh.framework.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClanDamageListener extends Module<ClanManager> implements Listener {

    public ClanDamageListener(ClanManager manager) {
        super(manager, "ClanDamageListener");
    }

    @EventHandler
    public void onCustomDamage(CustomDamageEvent event) {
        final Player damagee = event.getDamageePlayer();
        final Player damager = event.getDamagerPlayer();
        if(damagee == null || damager == null) {
            return;
        }
        if(!getManager().canHurt(damager, damagee, true)) {
            event.setCancelled(true);
        }
    }
}