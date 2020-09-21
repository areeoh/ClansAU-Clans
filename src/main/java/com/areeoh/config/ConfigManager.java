package com.areeoh.config;

import com.areeoh.ClansAUCore;
import com.areeoh.framework.Manager;
import com.areeoh.framework.Module;

import java.util.HashMap;

public class ConfigManager extends Manager<Module> {

    private HashMap<ConfigType, Config> configHashMap;

    public ConfigManager(ClansAUCore plugin) {
        super(plugin, "ConfigManager");
        configHashMap = new HashMap<>();

        for (ConfigType configType : ConfigType.values()) {
            Config config = new Config(plugin.getDataFolder(), configType.name);
            if (configType == ConfigType.CONFIG) {
                if (!config.fileExists()) {
                    config.createFile();
                    config.getConfig().set("MySQL.Server", "Server");
                    config.getConfig().set("MySQL.Username", "Username");
                    config.getConfig().set("MySQL.Password", "Password");
                    config.getConfig().set("MySQL.DatabaseName", "DatabaseName");
                }
            }
            config.loadFile();
            config.saveFile();
            configHashMap.put(configType, config);
        }
    }

    @Override
    public void registerModules() {
    }

    public enum ConfigType {
        CONFIG("Config");
        String name;

        ConfigType(String name) {
            this.name = name;
        }
    }

    public Config getConfig(ConfigType configType) {
        return configHashMap.get(configType);
    }
}