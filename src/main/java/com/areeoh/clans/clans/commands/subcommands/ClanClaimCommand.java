package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.ClanClaimEvent;
import com.areeoh.spigot.client.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilFormat;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ClanClaimCommand extends Command<Player> {

    public ClanClaimCommand(CommandManager manager) {
        super(manager, "ClanClaimCommand", Player.class);
        setCommand("claim");
        setIndex(1);
        setRequiredArgs(1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if(clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }
        final Client client = getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        boolean admin = client.isAdministrating();
        Chunk chunk = player.getLocation().getChunk();
        if(!admin) {
            if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
                UtilMessage.message(player, "Clans", "You can only claim in the Overworld.");
                return false;
            }
            if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to claim land.");
                return false;
            }
            if (clan.getClaims().size() >= (clan.getMaxClaims())) {
                UtilMessage.message(player, "Clans", "Your Clan cannot claim anymore land.");
                return false;
            }
        }
        if(clan.getClaims().contains(UtilFormat.chunkToString(chunk))) {
            UtilMessage.message(player, "Clans", "Your Clan already owns this land.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).getClan(chunk);
        if(!admin) {
            if (target != null) {
                UtilMessage.message(player, "Clans", "This Territory is owned by " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
                return false;
            }
            Set<String> clanSet = new HashSet<>();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    final Chunk lChunk = player.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    if (getManager(ClanManager.class).getClan(lChunk) != null) {
                        clanSet.add(getManager(ClanManager.class).getClan(lChunk).getName());
                    }
                }
            }
            if (clanSet.stream().anyMatch(s -> !clan.getName().equals(s))) {
                UtilMessage.message(player, "Clans", "You cannot claim next to enemy territory.");
                return false;
            }
        }
        if(clan.getClaims().size() > 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    final Chunk lChunk = player.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    if(getManager(ClanManager.class).getClan(lChunk) != null) {
                        Bukkit.getServer().getPluginManager().callEvent(new ClanClaimEvent(player, chunk, clan, !admin, true));
                        return true;
                    }
                }
            }
        } else {
            Bukkit.getServer().getPluginManager().callEvent(new ClanClaimEvent(player, chunk, clan, !admin, true));
            return true;
        }
        UtilMessage.message(player, "Clans", "You need to claim next to your own territory.");
        return false;
    }
}
