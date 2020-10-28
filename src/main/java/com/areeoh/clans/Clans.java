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
import com.areeoh.core.ClansAUCore;
import com.areeoh.core.database.DatabaseManager;
import com.areeoh.core.framework.Manager;
import com.areeoh.core.framework.Plugin;
import com.areeoh.core.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;

import java.util.Set;

public class Clans extends Plugin {

    private ClansAUCore plugin;

    @Override
    public void onEnable() {
        this.plugin = (ClansAUCore) Bukkit.getServer().getPluginManager().getPlugin("ClansAU-Core");

        final ClanRepository module = new ClanRepository(plugin.getManager(DatabaseManager.class));
        module.initialize(this);
        plugin.getManager(DatabaseManager.class).addModule(module);

        final ClanScoreboard clanScoreboard = new ClanScoreboard(plugin.getManager(ScoreboardManager.class));
        clanScoreboard.initialize(this);
        plugin.getManager(ScoreboardManager.class).addModule(clanScoreboard);

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
    }

    @Override
    public Set<Manager> getManagers() {
        return plugin.getManagers();
    }
}