package com.areeoh.clans.game.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.core.client.ClientManager;
import com.areeoh.spigot.core.utility.UtilFormat;
import com.areeoh.spigot.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClanIgnite extends GameModule implements Listener {
    public ClanIgnite(GameManager manager) {
        super(manager, "ClanIgnite");
    }

    @EventHandler
    public void blockFlint(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL)) {
            final Clan clan = getManager(ClanManager.class).getClan(event.getClickedBlock().getLocation().getChunk());
            if (clan != null) {
                if (clan.isAdmin()) {
                    if (!getManager(ClientManager.class).getOnlineClient(event.getPlayer().getUniqueId()).isAdministrating()) {
                        UtilMessage.message(event.getPlayer(), "Clans", "You cannot use " + ChatColor.GREEN + UtilFormat.cleanString(event.getPlayer().getItemInHand().getType().name()) + ChatColor.GRAY + " in " + ChatColor.YELLOW + getManager(ClanManager.class).getClanRelation(getManager(ClanManager.class).getClan(event.getPlayer()), clan).getSuffix() + clan.getName() + ChatColor.GRAY + ".");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL)
                && (event.getClickedBlock().getType() != Material.TNT)
                && (event.getClickedBlock().getType() != Material.NETHERRACK)) {
            if (!getManager(ClientManager.class).getOnlineClient(event.getPlayer().getUniqueId()).isAdministrating()) {
                UtilMessage.message(event.getPlayer(), "Game",
                        "You cannot use " + ChatColor.YELLOW + "Flint and Steel" + ChatColor.GRAY + " on this block type!");
                event.setCancelled(true);
            }
        }
    }
}