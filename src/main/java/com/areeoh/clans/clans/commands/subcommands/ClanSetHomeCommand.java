package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanSetHomeEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanSetHomeCommand extends Command<Player> {

    public ClanSetHomeCommand(CommandManager manager) {
        super(manager, "ClanSetHomeCommand", Player.class);
        setCommand("sethome");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Clan clan = getManager(ClanManager.class).getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an Admin or higher to set Clan Home.");
            return false;
        }
        if (getManager(ClanManager.class).getClan(player.getLocation().getChunk()) == null || !getManager(ClanManager.class).getClan(player.getLocation().getChunk()).equals(clan)) {
            UtilMessage.message(player, "Clans", "You must set your Clan Home in your own Territory.");
            return false;
        }
        if (player.getLocation().getY() < 40 || player.getLocation().getY() > 100) {
            UtilMessage.message(player, "Clans", "You can only set home between 40 and 100 Y");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanSetHomeEvent(player, clan));
        return true;
    }
}
