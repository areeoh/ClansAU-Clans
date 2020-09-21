package com.areeoh.clans.commands.subcommands;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.events.ClanAllyEvent;
import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.utility.UtilMessage;
import com.areeoh.utility.UtilTime;
import com.areeoh.pillaging.PillageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanAllyCommand extends Command<Player> {

    public ClanAllyCommand(CommandManager manager) {
        super(manager, "ClanAllyCommand", Player.class);
        setCommand("ally");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).searchClan(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the clan leader and admins can manage alliances.");
            return false;
        }
        if(clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot request an alliance with yourself.");
            return false;
        }
        if(getManager(PillageManager.class).isPillaging(clan, target) || getManager(PillageManager.class).isPillaged(clan, target)) {
            UtilMessage.message(player, "Clans", "You cannot ally " + ChatColor.LIGHT_PURPLE + "Clan " + target.getName() + ChatColor.GRAY + " while a Pillage is active.");
            return false;
        }
        if(clan.isAllied(target)) {
            UtilMessage.message(player, "Clans", "Your clan already has an alliance with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if(clan.isEnemy(target)) {
            UtilMessage.message(player, "Clans", "You must be neutral with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + " before requesting an alliance.");
            return false;
        }
        if(clan.getAllianceMap().size() + clan.getMemberMap().size() >= 8) {
            UtilMessage.message(player, "Clans", "Your clan has too many allies.");
            return false;
        }
        if(target.getAllianceMap().size() + target.getMemberMap().size() >= 8) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + " has too many allies.");
            return false;
        }
        if(target.getAllianceRequestMap().containsKey(clan.getName()) && !UtilTime.elapsed(target.getAllianceRequestMap().get(clan.getName()), 300000L)) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanAllyEvent(player, clan, target));
            return false;
        }
        if(!clan.getAllianceRequestMap().containsKey(target.getName())) {
            clan.getAllianceRequestMap().put(target.getName(), System.currentTimeMillis());
            UtilMessage.message(player, "Clans", "You requested alliance with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has requested alliance with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());
            target.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has requested alliance with your Clan.", null);
            return false;
        }
        UtilMessage.message(player, "Clans", "Your clan has already requested an alliance with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Clan to ally.");
    }
}
