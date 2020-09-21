package com.areeoh.map.commands;

import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.utility.UtilMessage;
import com.areeoh.utility.UtilTime;
import com.areeoh.map.MapManager;
import com.areeoh.map.renderer.MinimapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class LoadMapCommand extends Command<Player> {

    public LoadMapCommand(CommandManager manager) {
        super(manager, "LoadMapCommand", Player.class);
        setCommand("load");
        setRequiredArgs(1);
        setIndex(1);
    }

    @Override
    public boolean execute(Player player, String[] strings) {
        final long epoc = System.currentTimeMillis();

        MapView map = Bukkit.getMap((short) 0);
        if (map == null) {
            map = Bukkit.createMap(Bukkit.getWorld("world"));
        }
        MinimapRenderer minimapRenderer = (MinimapRenderer) map.getRenderers().get(0);

        minimapRenderer.getWorldCacheMap().clear();

        getManager(MapManager.class).loadMapData(minimapRenderer);

        UtilMessage.message(player, "Map", "Map loaded in " + ChatColor.GREEN + UtilTime.getTime(System.currentTimeMillis() - epoc, UtilTime.TimeUnit.SECONDS, 2) + ChatColor.GRAY + ".");
        return false;
    }
}