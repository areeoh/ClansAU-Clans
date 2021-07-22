package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanHomeEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanHomeCommand extends Command<Player> {

    public ClanHomeCommand(CommandManager manager) {
        super(manager, "ClanHomeCommand", Player.class);
        setCommand("home");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        if (clan.getHome() == null) {
            UtilMessage.message(player, "Clans", "Your Clan does not have a Home set.");
            return false;
        }
        if(!getManager(ClientManager.class).getOnlineClient(player.getUniqueId()).isAdministrating()) {
            final Clan land = getManager(ClanManager.class).getClan(player.getLocation().getChunk());
            if (land == null || land.getName().toLowerCase().contains(" spawn")) {
                UtilMessage.message(player, "Clans", "You can only use Clan Home from Spawn.");
                return false;
            }
        }
        Bukkit.getPluginManager().callEvent(new ClanHomeEvent(player, clan));
        return true;
    }
}