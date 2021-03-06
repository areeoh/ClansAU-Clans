package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanUnclaimEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanUnclaimCommand extends Command<Player> {

    public ClanUnclaimCommand(CommandManager manager) {
        super(manager, "ClanUnclaimCommand", Player.class);
        setCommand("unclaim");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
            UtilMessage.message(player, "Clans", "You must be an Admin or higher to unclaim land.");
            return false;
        }
        final Clan land = getManager(ClanManager.class).getClan(player.getLocation().getChunk());
        if (land == null || !land.equals(clan)) {
            UtilMessage.message(player, "Clans", "This Territory is not owned by you.");
            return false;
        }
        if(getManager(PillageManager.class).isGettingPillaged(clan)) {
            UtilMessage.message(player, "Clans", "You cannot unclaim land while you are getting Pillaged.");
            return false;
        }
        Bukkit.getPluginManager().callEvent(new ClanUnclaimEvent(player, clan));
        return true;
    }
}
