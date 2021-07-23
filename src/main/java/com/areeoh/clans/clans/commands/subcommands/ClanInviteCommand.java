package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanInviteEvent;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import com.areeoh.spigot.utility.UtilPlayer;
import com.areeoh.spigot.utility.UtilTime;
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
        if (!getManager(ClientManager.class).getOnlineClient(player.getUniqueId()).isAdministrating()) {
            if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to invite a Player.");
                return false;
            }
        }
        final Player target = UtilPlayer.searchPlayer(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (player.equals(player)) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself.");
            return false;
        }
        final Clan c = getManager(ClanManager.class).getClan(target.getUniqueId());
        if (c != null) {
            if (c.equals(clan)) {
                UtilMessage.message(player, "Clans", ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is already apart of your Clan.");
                return false;
            }
            final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, c);
            UtilMessage.message(player, "Clans", clanRelation.getSuffix() + target.getName() + ChatColor.GRAY + " is apart of " + clanRelation.getSuffix() + "Clan " + c.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if (clan.getInviteeMap().containsKey(target.getUniqueId()) && !UtilTime.elapsed(clan.getInviteeMap().get(target.getUniqueId()), 300000)) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has already been invited to your Clan.");
            return false;
        }
        if (clan.getMemberMap().size() + clan.getAllianceMap().size() >= 8) {
            UtilMessage.message(player, "Clans", "Your Clan has too many members/allies.");
            return false;
        }
        if (getManager(PillageManager.class).isGettingPillaged(clan)) {
            UtilMessage.message(player, "Clans", "You cannot invite a Player while your Clan has a Pillage active.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanInviteEvent(player, target, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Player to Invite.");
    }
}
