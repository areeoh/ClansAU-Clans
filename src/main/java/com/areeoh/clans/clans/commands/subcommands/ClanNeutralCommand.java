package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanNeutralEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilMessage;
import com.areeoh.spigot.core.utility.UtilTime;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanNeutralCommand extends Command<Player> {

    public ClanNeutralCommand(CommandManager manager) {
        super(manager, "ClanNeutralCommand", Player.class);
        setCommand("neutral");
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
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an Admin or higher to neutral a Clan.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).searchClan(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (!clan.isEnemy(target) && !clan.isAllied(target)) {
            UtilMessage.message(player, "Clans", "You are already neutral with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if(getManager(PillageManager.class).isPillaging(clan, target) || getManager(PillageManager.class).isPillaged(clan, target)) {
            UtilMessage.message(player, "Clans", "You cannot neutral " + ChatColor.LIGHT_PURPLE + "Clan " + target.getName() + ChatColor.GRAY + " while a Pillage is active.");
            return false;
        }
        if (clan.isAllied(target)) {
            Bukkit.getPluginManager().callEvent(new ClanNeutralEvent(player, clan, target));
            return true;
        }
        if (target.getNeutralRequestMap().containsKey(clan.getName()) && !UtilTime.elapsed(target.getNeutralRequestMap().get(clan.getName()), 300000)) {
            Bukkit.getPluginManager().callEvent(new ClanNeutralEvent(player, clan, target));
            return true;
        }
        if (!clan.getNeutralRequestMap().containsKey(target.getName())) {
            clan.getNeutralRequestMap().put(target.getName(), System.currentTimeMillis());
            UtilMessage.message(player, "Clans", "You requested neutrality with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has requested neutrality with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());
            target.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has requested neutrality with your Clan.");
            return true;
        }
        UtilMessage.message(player, "Clans", "You have already requested to neutral with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        return true;
    }

    @Override
    public void invalidArgsRequired(Player player) {
        UtilMessage.message(player, "Clans", "You did not input a Clan to Neutral.");
    }
}
