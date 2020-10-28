package com.areeoh.clans.pillaging.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.events.ClanPillageEndEvent;
import com.areeoh.clans.clans.events.ClanPillageStartEvent;
import com.areeoh.core.database.DatabaseManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.framework.Primitive;
import com.areeoh.core.framework.updater.Update;
import com.areeoh.core.framework.updater.Updater;
import com.areeoh.core.utility.UtilMessage;
import com.areeoh.clans.pillaging.Pillage;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Iterator;

public class PillageListener extends Module<PillageManager> implements Listener, Updater {

    public PillageListener(PillageManager manager) {
        super(manager, "PillageListener");

        addPrimitive("PillageTimeLength", new Primitive(900000L, 900000L));
    }

    @Update
    public void onUpdate() {
        if(getManager().getPillages().isEmpty()) {
            return;
        }
        for (Iterator<Pillage> it = getManager().getPillages().iterator(); it.hasNext(); ) {
            Pillage pillage = it.next();
            if(pillage.getTimeRemaining() <= 0) {
                it.remove();
                Bukkit.getServer().getPluginManager().callEvent(new ClanPillageEndEvent(pillage.getPillager(), pillage.getPillagee()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPillage(ClanPillageStartEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan killer = event.getPillager();
        final Clan dead = event.getPillagee();

        killer.getEnemyMap().remove(dead.getName());
        dead.getEnemyMap().remove(killer.getName());

        getManager().addPillage(new Pillage(killer, dead, getPrimitiveCasted(Long.class, "PillageTimeLength")));
        Bukkit.getOnlinePlayers().forEach(online -> {
            final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(killer, getManager(ClanManager.class).getClan(online.getUniqueId()));
            final ClanManager.ClanRelation deadRelation = getManager(ClanManager.class).getClanRelation(dead, getManager(ClanManager.class).getClan(online.getUniqueId()));
            UtilMessage.message(online, "Clans", clanRelation.getSuffix() + "Clan " + killer.getName() + ChatColor.GRAY + " has conquered " + deadRelation.getSuffix() + "Clan " + dead.getName() + ChatColor.GRAY + ".");
        });
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(killer);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(dead);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPillageEnd(ClanPillageEndEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan pillagee = event.getPillagee();
        final Clan pillager = event.getPillager();
        pillagee.inform(true, "Clans", "The Pillage on your Clan has finished!");
        pillager.inform(true, "Clans", "The Pillage on " + ChatColor.YELLOW + "Clan " + pillagee.getName() + ChatColor.GRAY + " has finished!");
    }
}
