package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.events.ClanDisbandEvent;
import com.areeoh.clans.clans.events.ClanEnergyUpdateEvent;
import com.areeoh.core.database.DatabaseManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.framework.updater.Update;
import com.areeoh.core.framework.updater.Updater;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Iterator;

public class ClanEnergyListener extends Module<ClanManager> implements Updater {

    public ClanEnergyListener(ClanManager manager) {
        super(manager, "ClanEnergyListener");
    }

    @Update(ticks = 720000)
    public void hourlySave() {
        for (Clan clan : getManager().getClanSet()) {
            getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnergy(clan);
        }
    }

    @Update(ticks = 1200)
    public void onUpdate() {
        for (Iterator<Clan> it = getManager().getClanSet().iterator(); it.hasNext(); ) {
            Clan clan = it.next();
            final double energyFromHours = clan.getEnergyFromHours(1) / 60;
            clan.setEnergy(clan.getEnergy() - (int) (energyFromHours));
            if (clan.getEnergy() <= 0) {
                Bukkit.getPluginManager().callEvent(new ClanDisbandEvent(null, clan));
                Bukkit.getOnlinePlayers().forEach(player -> {
                    final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, getManager().getClan(player.getUniqueId()));
                    UtilMessage.message(player, "Clans", clanRelation.getSuffix() + "Clans " + clan.getName() + ChatColor.GRAY + " has been disbanded for running out of energy!");
                });
                it.remove();
                continue;
            }
            Bukkit.getServer().getPluginManager().callEvent(new ClanEnergyUpdateEvent(clan));
        }
    }
}
