package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanHomeEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilMessage;
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
            UtilMessage.message(player, "Clans", "You are not in a clan.");
            return false;
        }
        if (clan.getHome().equals("")) {
            UtilMessage.message(player, "Clans", "Your clan has not set a home.");
            return false;
        }
        if (getManager(ClanManager.class).getClan(player.getLocation().getChunk()) == null) {
            UtilMessage.message(player, "Clans", "You can only use clan home from spawn.");
            return false;
        }
        if (!getManager(ClanManager.class).getClan(player.getLocation().getChunk()).getName().toLowerCase().contains(" spawn")) {
            UtilMessage.message(player, "Clans", "You can only use clan home from spawn.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanHomeEvent(player, clan));
        return true;
    }
}