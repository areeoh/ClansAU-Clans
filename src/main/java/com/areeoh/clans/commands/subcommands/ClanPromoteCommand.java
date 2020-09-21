package com.areeoh.clans.commands.subcommands;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.events.ClanPromoteEvent;
import com.areeoh.client.Client;
import com.areeoh.client.ClientManager;
import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanPromoteCommand extends Command<Player> {

    public ClanPromoteCommand(CommandManager manager) {
        super(manager, "ClanPromoteCommand", Player.class);
        setCommand("promote");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player);
        if(clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        final Client target = getManager(ClientManager.class).searchClient(player, args[1], true);
        if(target == null) {
            return false;
        }
        if(!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an admin or higher to promote a player.");
            return false;
        }
        if(player.getUniqueId().equals(target.getUUID())) {
            UtilMessage.message(player, "Clans", "You cannot promote yourself.");
            return false;
        }
        if(!clan.equals(getManager(ClanManager.class).getClan(target.getUUID()))) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not apart of your clan.");
            return false;
        }
        if(clan.getMemberRole(player.getUniqueId()).ordinal() <= clan.getMemberRole(target.getUUID()).ordinal()) {
            UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if(clan.getMemberRole(target.getUUID()) == Clan.MemberRole.LEADER) {
            UtilMessage.message(player, "Clans", "You cannot promote " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " any further.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanPromoteEvent(player, target, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a player to promote.");
    }
}
