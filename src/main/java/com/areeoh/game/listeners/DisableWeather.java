package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class DisableWeather extends GameModule implements Listener {
    public DisableWeather(GameManager manager) {
        super(manager, "DisableWeather");
    }

    @EventHandler
    public void setSunny(WeatherChangeEvent event) {
        if (!event.getWorld().hasStorm()) {
            event.setCancelled(true);
        }
    }
}
