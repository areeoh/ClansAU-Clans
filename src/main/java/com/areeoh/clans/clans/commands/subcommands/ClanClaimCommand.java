package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.events.ClanClaimEvent;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.client.Client;
import com.areeoh.spigot.core.client.ClientManager;
import com.areeoh.spigot.core.framework.commands.Command;
import com.areeoh.spigot.core.framework.commands.CommandManager;
import com.areeoh.spigot.core.utility.UtilFormat;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;

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
        if(!client.isAdministrating()) {
            if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
                UtilMessage.message(player, "Clans", "You can only claim in the overworld.");
                return false;
            }
            if (!clan.hasRole(player.getUniqueId(), Clan.MemberRole.ADMIN)) {
                UtilMessage.message(player, "Clans", "You need to be a Clan admin to claim land.");
                return false;
            }
            if (clan.getClaims().size() >= (clan.getMaxClaims())) {
                UtilMessage.message(player, "Clans", "Your Clan cannot claim anymore land.");
                return false;
            }
        }
        if(clan.getClaims().contains(UtilFormat.chunkToString(player.getLocation().getChunk()))) {
            UtilMessage.message(player, "Clans", "Your Clan already owns this land.");
            return false;
        }
        final Clan target = getManager(ClanManager.class).getClan(player.getLocation().getChunk());
        if(!client.isAdministrating()) {
            if (target != null) {
                UtilMessage.message(player, "Clans", "This land is owned by " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
                return false;
            }
            HashSet<String> clanSet = new HashSet<>();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    final Chunk lChunk = player.getWorld().getChunkAt(player.getLocation().getChunk().getX() + x, player.getLocation().getChunk().getZ() + z);
                    if (getManager(ClanManager.class).getClan(lChunk) != null) {
                        clanSet.add(getManager(ClanManager.class).getClan(lChunk).getName());
                    }
                }
            }
            if (clanSet.stream().anyMatch(s -> !clan.getName().equals(s))) {
                UtilMessage.message(player, "Clans", "You cannot claim next to enemy land.");
                return false;
            }
        }
        if(clan.getClaims().size() > 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    final Chunk lChunk = player.getWorld().getChunkAt(player.getLocation().getChunk().getX() + x, player.getLocation().getChunk().getZ() + z);
                    if(getManager(ClanManager.class).getClan(lChunk) != null) {
                        Bukkit.getServer().getPluginManager().callEvent(new ClanClaimEvent(player, clan));
                        return true;
                    }
                }
            }
        } else {
            Bukkit.getServer().getPluginManager().callEvent(new ClanClaimEvent(player, clan));
            return true;
        }
        UtilMessage.message(player, "Clans", "You need to claim next to your own territory.");
        return false;
    }
}
