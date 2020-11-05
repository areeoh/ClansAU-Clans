package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanKickEvent;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.spigot.core.client.ClientManager;
import com.areeoh.spigot.core.client.OfflineClient;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanKickCommand extends Command<Player> {

    public ClanKickCommand(CommandManager manager) {
        super(manager, "ClanKickCommand", Player.class);
        setCommand("kick");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a clan.");
            return false;
        }
        final OfflineClient target = getManager(ClientManager.class).searchClient(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (!clan.equals(getManager(ClanManager.class).getClan(target.getUUID()))) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not apart of your clan.");
            return false;
        }
        if(target.getUUID().equals(player.getUniqueId())) {
            UtilMessage.message(player, "Clans", "You cannot kick yourself.");
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            return false;
        }
        if(getManager(PillageManager.class).isGettingPillaged(clan)) {
            UtilMessage.message(player, "Clans", "You cannot kick a player while you are getting pillaged.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanKickEvent(player, target, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a player to kick.");
    }
}