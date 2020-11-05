package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.core.framework.updater.Update;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class CustomDayNight extends GameModule implements Listener {

    public CustomDayNight(GameManager manager) {
        super(manager, "CustomDayNight");
    }

    @Update(ticks = 5)
    public void onUpdate() {
        if (Bukkit.getWorld("world").getTime() > 13000L) {
            //Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 12L);
        }
    }
}
