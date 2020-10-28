package com.areeoh.clans.clans.listeners;

import com.areeoh.champions.skills.events.SkillActivateEvent;
import com.areeoh.champions.skills.events.SkillChangeBlockEvent;
import com.areeoh.champions.skills.events.SkillEffectEntityEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClanSkillListener extends Module<ClanManager> implements Listener {

    public ClanSkillListener(ClanManager manager) {
        super(manager, "ClanSkillListener");
    }

    @EventHandler
    public void onSkillActivate(SkillActivateEvent event) {
        Clan clan = getManager().getClan(event.getPlayer().getLocation());
        if(clan != null && clan.isAdmin() && clan.isSafe(event.getPlayer().getLocation())) {
            UtilMessage.message(event.getPlayer(), "Restriction", "You are not allowed to cast abilities here!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSkillBlockEffect(SkillChangeBlockEvent event) {
        Clan clan = getManager().getClan(event.getBlock().getLocation());
        if(clan != null && clan.isAdmin()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSkillEntityEffect(SkillEffectEntityEvent event) {
        Clan clan = getManager().getClan(event.getPlayer());
        if(clan == null) {
            return;
        }
        for (Player member : clan.getOnlinePlayers()) {
            event.getFriendlyEntities().add(member);
        }
        for (String c : clan.getAllianceMap().keySet()) {
            for (Player player : getManager().getClan(c).getOnlinePlayers()) {
                event.getFriendlyEntities().add(player);
            }
        }
    }
}