package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanDemoteEvent;
import com.areeoh.shared.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanDemoteCommand extends Command<Player> {

    public ClanDemoteCommand(CommandManager manager) {
        super(manager, "ClanDemoteCommand", Player.class);
        setCommand("demote");
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
        Client target = getManager(ClanManager.class).searchMember(player, args[1], true);
        if (target == null) {
            return false;
        }
        if (!getManager(ClientManager.class).getClient(player.getUniqueId()).isAdministrating()) {
            if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to demote a Player.");
                return false;
            }
            if (player.getUniqueId().equals(target.getUUID())) {
                UtilMessage.message(player, "Clans", "You cannot demote yourself.");
                return false;
            }
            if (!clan.equals(getManager(ClanManager.class).getClan(target.getUUID()))) {
                UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not apart of your Clan.");
                return false;
            }
            if (clan.getMemberRole(player.getUniqueId()).ordinal() <= clan.getMemberRole(target.getUUID()).ordinal()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                return false;
            }
            if (clan.getMemberRole(target.getUUID()).ordinal() == 0) {
                UtilMessage.message(player, "Clans", "You cannot demote " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " any further.");
                return false;
            }
        }
        Bukkit.getPluginManager().callEvent(new ClanDemoteEvent(player, target, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Player to Demote.");
    }
}
