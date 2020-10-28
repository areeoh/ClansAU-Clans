package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanInviteEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.commands.Command;
import com.areeoh.core.framework.commands.CommandManager;
import com.areeoh.core.utility.UtilMessage;
import com.areeoh.core.utility.UtilPlayer;
import com.areeoh.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanInviteCommand extends Command<Player> {

    public ClanInviteCommand(CommandManager manager) {
        super(manager, "ClanInviteCommand", Player.class);
        setCommand("invite");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an admin or higher to invite players to your Clan.");
            return false;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[1], true);
        if(target == null) {
            return false;
        }
        if(player.getUniqueId().equals(target.getUniqueId())) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself.");
            return false;
        }
        final Clan c = getManager(ClanManager.class).getClan(target.getUniqueId());
        if(c != null) {
            final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, c);
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is apart of " + clanRelation.getSuffix() + "Clan " + c.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if(clan.getInviteeMap().containsKey(target.getUniqueId()) && !UtilTime.elapsed(clan.getInviteeMap().get(target.getUniqueId()), 300000)) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has already been invited to your Clan.");
            return false;
        }
        if(clan.getMemberMap().size() + clan.getAllianceMap().size() >= 8) {
            UtilMessage.message(player, "Clans", "Your clan has too many members.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanInviteEvent(player, target, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a player to invite.");
    }
}