package com.areeoh.clans.world;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.client.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.teleport.TeleportManager;
import com.areeoh.spigot.utility.UtilMath;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommandManager extends CommandManager {

    private final List<Location> locations = new ArrayList<>();

    public SpawnCommandManager(Plugin plugin) {
        super(plugin, "SpawnCmdManager");
        locations.add(new Location(Bukkit.getWorld("world"), 0.5D, 122.0D, -500.5D, 0.0F, 0.0F));
        locations.add(new Location(Bukkit.getWorld("world"), 0.5D, 122.0D, 500.5D, 180.0F, 0.0F));
    }

    @Override
    public void registerModules() {
        addModule(new SpawnCommand(this));
    }

    class SpawnCommand extends Command<Player> {

        public SpawnCommand(CommandManager manager) {
            super(manager, "BaseCommand", Player.class);
            setCommand("spawn");
            setIndex(0);
            setRequiredArgs(0);
        }

        @Override
        public boolean execute(Player player, String[] strings) {
            Client client = getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
            if(!client.isAdministrating()) {
                if(getManager(ClanManager.class).getClan(player.getLocation()) != null) {
                    UtilMessage.message(player, "Clans", "You can only teleport to Spawn from Wilderness.");
                    return false;
                }
                if(getPlugin().getManager(TeleportManager.class).isTeleporting(player)) {
                    UtilMessage.message(player, "Teleport", "You are already teleporting to Spawn.");
                    return false;
                }
            }
            getManager(TeleportManager.class).teleport(player, locations.get(UtilMath.randomInt(0, locations.size())), "Spawn", 60000L);
            return false;
        }
    }
}