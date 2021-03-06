package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanDisbandEvent;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanDisbandCommand extends Command<Player> {

    public ClanDisbandCommand(CommandManager manager) {
        super(manager, "ClanDisbandCommand", Player.class);
        setCommand("disband");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player);
        if(clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        if(!getManager(ClientManager.class).getOnlineClient(player.getUniqueId()).isAdministrating()) {
            if (clan.getMemberRole(player.getUniqueId()).ordinal() < Clan.MemberRole.LEADER.ordinal()) {
                UtilMessage.message(player, "Clans", "You must be Leader to disband your Clan.");
                return false;
            }
            if (getManager(PillageManager.class).isGettingPillaged(clan)) {
                UtilMessage.message(player, "Clans", "You cannot disband while you are getting Pillaged.");
                return false;
            }
        }
        //TODO CHECK IF THEY ARE GETTING DOMMED
        //TODO COMMAND CONFIRM
        Bukkit.getServer().getPluginManager().callEvent(new ClanDisbandEvent(player, clan, ClanDisbandEvent.DisbandReason.PLAYER));
        return true;
    }
}
