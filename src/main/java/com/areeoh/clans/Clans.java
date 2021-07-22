package com.areeoh.clans;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.clans.clans.commands.AllyChatCommandManager;
import com.areeoh.clans.clans.commands.ClanChatCommandManager;
import com.areeoh.clans.clans.commands.ClanCmdManager;
import com.areeoh.clans.fishing.FishingManager;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.map.MapManager;
import com.areeoh.clans.map.commands.MapCommandManager;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.clans.scoreboard.ClanScoreboard;
import com.areeoh.clans.shops.ShopManager;
import com.areeoh.clans.shops.ShopRepository;
import com.areeoh.clans.shops.commands.ShopCommandManager;
import com.areeoh.clans.world.SpawnCommandManager;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.repository.RepositoryManager;
import com.areeoh.spigot.scoreboard.ScoreboardManager;

public class Clans extends Plugin {

    @Override
    public void registerManagers() {
        addModuleToManager(new ClanScoreboard(getManager(ScoreboardManager.class)), getManager(ScoreboardManager.class));

        addManager(new GameManager(this));
        addManager(new ClanManager(this));
        addManager(new PillageManager(this));
        addManager(new FishingManager(this));
        addManager(new MapManager(this));
        addManager(new ShopManager(this));

        // Commands
        addManager(new ClanCmdManager(this));
        addManager(new ClanChatCommandManager(this));
        addManager(new AllyChatCommandManager(this));
        addManager(new MapCommandManager(this));
        addManager(new SpawnCommandManager(this));
        addManager(new ShopCommandManager(this));

        addModuleToManager(new ShopRepository(getManager(RepositoryManager.class)), getManager(RepositoryManager.class));
        addModuleToManager(new ClanRepository(getManager(RepositoryManager.class)), getManager(RepositoryManager.class));
    }
}