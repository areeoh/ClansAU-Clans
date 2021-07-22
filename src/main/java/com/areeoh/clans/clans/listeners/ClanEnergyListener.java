package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.events.ClanDisbandEvent;
import com.areeoh.clans.clans.events.ClanEnergyUpdateEvent;
import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.framework.updater.Update;
import com.areeoh.spigot.framework.updater.Updater;
import com.areeoh.spigot.repository.RepositoryManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ClanEnergyListener extends Module<ClanManager> implements Updater {

    public ClanEnergyListener(ClanManager manager) {
        super(manager, "ClanEnergyListener");
    }

    @Update(ticks = 720000)
    public void hourlySave() {
        for (Clan clan : getManager().getClanSet()) {
            getManager(RepositoryManager.class).getModule(ClanRepository.class).updateEnergy(clan);
        }
    }

    @Update(ticks = 1200)
    public void onUpdate() {
        for (Clan clan : getManager().getClanSet()) {
            final double energyFromHours = clan.getEnergyFromHours(1) / 60;
            clan.setEnergy(clan.getEnergy() - (int) (energyFromHours));
            if (clan.getEnergy() <= 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, getManager().getClan(player.getUniqueId()));
                    UtilMessage.message(player, "Clans", clanRelation.getSuffix() + "Clans " + clan.getName() + ChatColor.GRAY + " has been disbanded for running out of energy!");
                });
                Bukkit.getPluginManager().callEvent(new ClanDisbandEvent(null, clan, ClanDisbandEvent.DisbandReason.ENERGY));
                continue;
            }
            Bukkit.getServer().getPluginManager().callEvent(new ClanEnergyUpdateEvent(clan));
        }
    }
}
