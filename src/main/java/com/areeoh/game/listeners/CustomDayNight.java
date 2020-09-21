package com.areeoh.game.listeners;

import com.areeoh.framework.updater.Update;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
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
