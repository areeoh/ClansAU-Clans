package com.areeoh.clans.pillaging;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.events.ClanPillageStartEvent;
import com.areeoh.clans.pillaging.listeners.PillageAnnouncer;
import com.areeoh.clans.pillaging.listeners.PillageDeathListener;
import com.areeoh.clans.pillaging.listeners.PillageListener;
import com.areeoh.spigot.framework.Manager;
import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.repository.RepositoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;

public class PillageManager extends Manager<Module> {

    private final Set<Pillage> pillages = new HashSet<>();

    public PillageManager(Plugin plugin) {
        super(plugin, "PillagerManager");
    }

    @Override
    public void registerModules() {
        addModule(new PillageDeathListener(this));
        addModule(new PillageListener(this));
        addModule(new PillageAnnouncer(this));
    }

    public void giveDomPoint(Clan killer, Clan dead, int points) {
        if(killer.getEnemyMap().containsKey(dead.getName())) {
            if(dead.getEnemyMap().get(killer.getName()) <= killer.getEnemyMap().get(dead.getName())) {
                killer.getEnemyMap().put(dead.getName(), killer.getEnemyMap().get(dead.getName()) + points);
                killer.inform(true, "Clans", "Your Clan gained Dominance against " + ChatColor.RED + "Clan " + dead.getName() + " " + killer.getDominanceString(dead) + ChatColor.GRAY + ".");
                dead.inform(true, "Clans", "Your Clan lost Dominance against " + ChatColor.RED + "Clan " + killer.getName() + " " + dead.getDominanceString(killer) + ChatColor.GRAY + ".");
            } else if(dead.getEnemyMap().get(killer.getName()) > killer.getEnemyMap().get(dead.getName())) {
                dead.getEnemyMap().put(killer.getName(), dead.getEnemyMap().get(killer.getName()) - points);
                dead.inform(true, "Clans", "Your Clan lost Dominance against " + ChatColor.RED + "Clan " + killer.getName() + " " + dead.getDominanceString(killer) + ChatColor.GRAY + ".");
                killer.inform(true, "Clans", "Your Clan recovered Dominance against " + ChatColor.RED + "Clan " + dead.getName() + " " + killer.getDominanceString(dead) + ChatColor.GRAY + ".");
            }
            getManager(RepositoryManager.class).getModule(ClanRepository.class).updateEnemies(killer);
            getManager(RepositoryManager.class).getModule(ClanRepository.class).updateEnemies(dead);
            if(killer.getEnemyMap().get(dead.getName()) >= 16) {
                Bukkit.getServer().getPluginManager().callEvent(new ClanPillageStartEvent(killer, dead));
            }
        }
    }

    public void addPillage(Pillage pillage) {
        pillages.add(pillage);
    }

    public Set<Pillage> getPillages() {
        return pillages;
    }

    public boolean isPillaging(Clan clan, Clan other) {
        if(clan == null || other == null) {
            return false;
        }
        return pillages.stream().anyMatch(pillage -> pillage.getPillager().equals(clan) && pillage.getPillagee().equals(other));
    }

    public boolean isPillaged(Clan clan, Clan other) {
        if(clan == null || other == null) {
            return false;
        }
        return pillages.stream().anyMatch(pillage -> pillage.getPillager().equals(other) && pillage.getPillagee().equals(clan));
    }

    public boolean isGettingPillaged(Clan clan) {
        if(clan == null) {
            return false;
        }
        return pillages.stream().anyMatch(pillage -> pillage.getPillagee().equals(clan));
    }
}
