package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.core.client.ClientManager;
import com.areeoh.spigot.core.framework.Module;
import com.areeoh.spigot.core.utility.UtilFormat;
import com.areeoh.spigot.core.utility.UtilMessage;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ClanPlaceBlockListener extends Module<ClanManager> implements Listener {

    public ClanPlaceBlockListener(ClanManager manager) {
        super(manager, "ClanPlaceBlockListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        final Clan lClan = getManager().getClan(block.getLocation());

        if (lClan == null) {
            return;
        }

        if (!lClan.equals(clan)) {
            if (getManager(PillageManager.class).isPillaging(clan, lClan)) {
                return;
            }
            if (getManager(ClientManager.class).getOnlineClient(player.getUniqueId()).isAdministrating()) {
                return;
            }
            final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, lClan);
            UtilMessage.message(player, "Clans", "You cannot place " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString()) + ChatColor.GRAY + " in " + clanRelation.getSuffix() + (lClan.isAdmin() ? "" : "Clan ") + lClan.getName() + ChatColor.GRAY + ".");
            event.setCancelled(true);
        } else if (lClan.equals(clan) && clan.getMemberRole(player.getUniqueId()) == Clan.MemberRole.RECRUIT) {
            UtilMessage.message(player, "Clans", "Clan Recruits cannot place blocks.");
            event.setCancelled(true);
        }
    }
}