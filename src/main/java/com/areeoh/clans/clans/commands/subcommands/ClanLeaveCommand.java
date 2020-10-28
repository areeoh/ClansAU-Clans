package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanDisbandEvent;
import com.areeoh.clans.clans.events.ClanLeaveEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.commands.Command;
import com.areeoh.core.framework.commands.CommandManager;
import com.areeoh.core.utility.UtilMessage;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanLeaveCommand extends Command<Player> {

    public ClanLeaveCommand(CommandManager manager) {
        super(manager, "ClanLeaveCommand", Player.class);
        setCommand("leave");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if(clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return true;
        }
        if(getManager(PillageManager.class).isGettingPillaged(clan)) {
            UtilMessage.message(player, "Clans", "You cannot leave your Clan while you are getting pillaged.");
            return true;
        }
        if(clan.getMemberRole(player.getUniqueId()) == Clan.MemberRole.LEADER && clan.getMemberMap().size() == 1) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanDisbandEvent(player, clan));
            return true;
        }
        if(clan.getMemberRole(player.getUniqueId()) == Clan.MemberRole.LEADER && clan.getMemberMap().size() > 1) {
            UtilMessage.message(player, "Clans", "You must pass on Leadership before leaving.");
            return true;
        }
        Bukkit.getPluginManager().callEvent(new ClanLeaveEvent(player, clan));
        return true;
    }
}
