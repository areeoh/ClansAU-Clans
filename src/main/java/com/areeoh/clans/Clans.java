package com.areeoh.clans;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.commands.ClanCmdManager;
import com.areeoh.clans.fishing.FishingManager;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.map.MapManager;
import com.areeoh.clans.map.commands.MapCommandManager;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.clans.scoreboard.ClanScoreboard;
import com.areeoh.spigot.core.ClansAUCore;
import com.areeoh.spigot.core.framework.Manager;
import com.areeoh.spigot.core.framework.Plugin;
import com.areeoh.spigot.core.repository.RepositoryManager;
import com.areeoh.spigot.core.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;

import java.util.Set;

public class Clans extends Plugin {

    private ClansAUCore plugin;

    @Override
    public void onEnable() {
        this.plugin = (ClansAUCore) Bukkit.getServer().getPluginManager().getPlugin("ClansAU-Core");

        final ClanScoreboard clanScoreboard = new ClanScoreboard(plugin.getManager(ScoreboardManager.class));
        addModuleToManager(clanScoreboard, getManager(ScoreboardManager.class));

        registerManagers();
    }

    private void registerManagers() {
        addManager(new GameManager(this));
        addManager(new ClanManager(this));
        addManager(new PillageManager(this));
        addManager(new FishingManager(this));
        addManager(new MapManager(this));

        // Commands
        addManager(new ClanCmdManager(this));
        addManager(new MapCommandManager(this));

        addModuleToManager(new ClanRepository(getManager(RepositoryManager.class)), plugin.getManager(RepositoryManager.class));
    }

    @Override
    public Set<Manager> getManagers() {
        return plugin.getManagers();
    }
}