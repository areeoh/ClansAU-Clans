package com.areeoh;

import com.areeoh.blockregen.BlockRegenManager;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.ClanRepository;
import com.areeoh.clans.commands.ClanCmdManager;
import com.areeoh.config.ConfigManager;
import com.areeoh.database.DatabaseManager;
import com.areeoh.fishing.FishingManager;
import com.areeoh.framework.Manager;
import com.areeoh.game.GameManager;
import com.areeoh.map.MapManager;
import com.areeoh.map.commands.MapCommandManager;
import com.areeoh.menu.MenuManager;
import com.areeoh.pillaging.PillageManager;
import com.areeoh.scoreboard.ClanScoreboardManager;
import com.areeoh.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Clans extends JavaPlugin {

    private ClansAUCore clansAUCore;
    private Set<Manager> managers = new HashSet<>();

    @Override
    public void onEnable() {
        this.clansAUCore = (ClansAUCore) Bukkit.getServer().getPluginManager().getPlugin("ClansAU-Core");

        final ClanRepository module = new ClanRepository(clansAUCore.getManager(DatabaseManager.class));
        module.initialize(this);
        clansAUCore.getManager(DatabaseManager.class).addModule(module);

        registerManagers();

        for (Manager manager : managers) {
            if (manager.isEnabled()) {
                try {
                    manager.initialize(this);
                    System.out.println(manager.getName() + " initialised.");
                } catch (Exception ex) {
                    UtilMessage.log("Error", "Failed to load " + manager.getName());
                }
            }
            clansAUCore.getManagers().add(manager);
        }
    }

    @Override
    public void onDisable() {
        for (Manager manager : managers) {
            manager.shutdown();
        }
    }

    private void registerManagers() {
        managers.add(new GameManager(clansAUCore));
        managers.add(new BlockRegenManager(clansAUCore));
        managers.add(new ClanManager(clansAUCore));
        managers.add(new PillageManager(clansAUCore));
        managers.add(new FishingManager(clansAUCore));
        managers.add(new ClanScoreboardManager(clansAUCore));
        managers.add(new MapManager(clansAUCore));
        managers.add(new ConfigManager(clansAUCore));
        managers.add(new MenuManager(clansAUCore));

        // Commands
        managers.add(new ClanCmdManager(clansAUCore));
        managers.add(new MapCommandManager(clansAUCore));
    }
}