package com.areeoh.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private File folder;
    private File file;
    private YamlConfiguration config;

    Config(File folder, String fileName) {
        this.folder = folder;
        this.file = new File(folder, fileName + ".yml");
        this.config = new YamlConfiguration();
    }

    public Config(File folder, File file) {
        this.folder = folder;
        this.file = file;
        this.config = new YamlConfiguration();
    }

    void createFile() {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Config File Error.");
            }
        }
    }

    boolean fileExists() {
        return file.exists();
    }

    public void loadFile() {
        try {
            config.load(file);
        } catch (NullPointerException | IOException | InvalidConfigurationException e) {
            createFile();
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Failed to save File " + file.getName() + ".");
        }
    }

    public File getFolder() {
        return folder;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}