package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanEnemyEvent;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanEnemyCommand extends Command<Player> {

    public ClanEnemyCommand(CommandManager manager) {
        super(manager, "ClanEnemyCommand", Player.class);
        setCommand("enemy");
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
            UtilMessage.message(player, "Clans", "You must be an Admin or higher to enemy a Clan.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).searchClan(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot enemy yourself.");
            return false;
        }
        if (clan.isEnemy(target)) {
            UtilMessage.message(player, "Clans", "You are already enemies with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if (target.isAdmin()) {
            UtilMessage.message(player, "Clans", "You cannot wage war with Admin Clans.");
            return false;
        }
        if (getManager(PillageManager.class).isPillaging(clan, target) || getManager(PillageManager.class).isPillaged(clan, target)) {
            UtilMessage.message(player, "Clans", "You cannot enemy " + ChatColor.LIGHT_PURPLE + "Clan " + target.getName() + ChatColor.GRAY + " while a Pillage is active.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanEnemyEvent(player, clan, target));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Clan to Enemy.");
    }
}
