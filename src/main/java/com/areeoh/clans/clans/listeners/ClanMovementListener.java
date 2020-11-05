package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.events.ClanMoveEvent;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.framework.Module;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClanMovementListener extends Module<ClanManager> implements Listener {

    public ClanMovementListener(ClanManager manager) {
        super(manager, "ClanMovementListener");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location to = event.getTo();
        final Location from = event.getFrom();
        final Clan clanTo = getManager(ClanManager.class).getClan(to);
        final Clan clanFrom = getManager(ClanManager.class).getClan(from);
        if(clanTo == null && clanFrom == null) {
            return;
        }
        if(clanFrom == null || !clanFrom.equals(clanTo)) {
            Bukkit.getPluginManager().callEvent(new ClanMoveEvent(player, clanFrom, clanTo, from, to));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanMove(ClanMoveEvent event) {
        if(event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final Location location = event.getLocTo();
        UtilMessage.message(player, "Territory", getTerritoryString(player, location));
    }

    public String getTerritoryString(Player player, Location location) {
        String owner = ChatColor.YELLOW + "Wilderness";
        String append = "";
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        final Clan target = getManager(ClanManager.class).getClan(location);
        if(target != null) {
            ClanManager.ClanRelation relation = getManager(ClanManager.class).getClanRelation(clan, target);
            owner = relation.getSuffix() + target.getName();
            if(target.isAdmin()) {
                owner = ChatColor.WHITE + target.getName();
                if(target.getName().equalsIgnoreCase("Outskirts")) {
                    owner = ChatColor.YELLOW + target.getName();
                }
                if(target.isSafe(location)) {
                    append = ChatColor.WHITE + "(" + ChatColor.AQUA + "Safe" + ChatColor.WHITE + ")";
                }
            }
            if(relation == ClanManager.ClanRelation.ALLY_TRUSTED) {
                append = ChatColor.WHITE + "(" + ChatColor.YELLOW + "Trusted" + ChatColor.WHITE + ")";
            }
            if(relation == ClanManager.ClanRelation.ENEMY) {
                if(clan != null) {
                    append = clan.getDominanceString(target);
                }
            }
            if(target.getName().equalsIgnoreCase("Fields") || target.getName().equalsIgnoreCase("Lake")) {
                append = ChatColor.RED.toString() + ChatColor.BOLD + "                    Warning! " + ChatColor.GRAY.toString() + ChatColor.BOLD + "PvP Hotspot";
            }
        }
        return owner + " " + append;
    }
}