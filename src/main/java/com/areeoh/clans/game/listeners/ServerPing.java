package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing extends GameModule implements Listener {

    public ServerPing(GameManager manager) {
        super(manager, "ServerPing");
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMaxPlayers(Math.max(100, Bukkit.getOnlinePlayers().size()));
        event.setMotd(ChatColor.translateAlternateColorCodes('&',
                "&6&lClansAU &8» &aAustralian Clans Server" +
                        "                  &7[1.8.9] &fVisit our Website at &ehttps://clansau.net"));
    }
}