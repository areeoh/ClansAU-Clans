package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanJoinEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilMessage;
import com.areeoh.spigot.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanJoinCommand extends Command<Player> {

    public ClanJoinCommand(CommandManager manager) {
        super(manager, "ClanJoinCommand", Player.class);
        setCommand("join");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if(clan != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).searchClan(player, args[1], true);
        if(target == null) {
            return false;
        }
        /*
        if(target.isAdmin()) {
            UtilMessage.message(player, "Clans", "You cannot join Admin Clans.");
            return false;
        }
        */
        if(target.getMemberMap().size() + target.getAllianceMap().size() >= 8) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + " has too many members/allies.");
            return false;
        }
        if (target.getInviteeMap().containsKey(player.getUniqueId()) && !UtilTime.elapsed(target.getInviteeMap().get(player.getUniqueId()), 300000)) {
            Bukkit.getPluginManager().callEvent(new ClanJoinEvent(player, target));
            return true;
        }
        UtilMessage.message(player, "Clans", "You have not been invited to " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
        return false;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Clan to Join.");
    }
}
