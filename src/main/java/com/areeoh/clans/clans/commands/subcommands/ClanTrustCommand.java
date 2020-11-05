package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanRevokeTrustEvent;
import com.areeoh.clans.clans.events.ClanTrustEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilMessage;
import com.areeoh.spigot.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanTrustCommand extends Command<Player> {

    public ClanTrustCommand(CommandManager manager) {
        super(manager, "ClanTrustCommand", Player.class);
        setCommand("trust");
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
        final Clan target = getManager(ClanManager.class).searchClan(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an admin or higher to manage trust.");
            return false;
        }
        if (clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot trust yourself.");
            return false;
        }
        if (clan.isTrusted(target)) {
            Bukkit.getPluginManager().callEvent(new ClanRevokeTrustEvent(player, clan, target));
            return true;
        }
        final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, target);
        if(!clan.isAllied(target)) {
            UtilMessage.message(player, "Clans", "You must be allies with " + clanRelation.getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + " before trusting them.");
            return true;
        }
        if (target.getTrustRequestMap().containsKey(clan.getName()) && !UtilTime.elapsed(target.getTrustRequestMap().get(clan.getName()), 300000)) {
            Bukkit.getPluginManager().callEvent(new ClanTrustEvent(player, clan, target));
            return true;
        }
        if (!clan.getTrustRequestMap().containsKey(target.getName())) {
            clan.getTrustRequestMap().put(target.getName(), System.currentTimeMillis());
            UtilMessage.message(player, "Clans", "You requested trust with " + clanRelation.getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has requested trust with " + clanRelation.getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());
            target.inform(true, "Clans", clanRelation.getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has requested trust with your Clan.");
            return true;
        }
        UtilMessage.message(player, "Clans", "You have already requested to trust with " + clanRelation.getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        return false;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a clan to trust.");
    }
}
