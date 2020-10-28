package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.client.Client;
import com.areeoh.core.client.ClientManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.utility.UtilFormat;
import com.areeoh.core.utility.UtilMessage;
import com.areeoh.clans.pillaging.PillageManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ClanBreakBlockListener extends Module<ClanManager> implements Listener {

    public ClanBreakBlockListener(ClanManager manager) {
        super(manager, "ClanBreakBlockListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Block block = event.getBlock();
        final Clan lClan = getManager().getClan(block.getLocation());
        if(lClan == null) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if(!lClan.equals(clan)) {
            final Client client = getManager(ClientManager.class).getClient(player.getUniqueId());
            //if(!client.isAdministrating() && !lClan.isAdmin() && CHECK IF FARMING BLOCK) {
            if(client.isAdministrating()) {
                return;
            }
            if(getManager(PillageManager.class).isPillaging(clan, lClan)) {
                return;
            }
            final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, lClan);
            UtilMessage.message(player, "Clans", "You cannot break " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + " in " + clanRelation.getSuffix() + (lClan.isAdmin() ? "" : "Clan ") + lClan.getName() + ChatColor.GRAY + ".");
            event.setCancelled(true);
        } else if (lClan.equals(clan) && clan.getMemberRole(player.getUniqueId()) == Clan.MemberRole.RECRUIT) {
            UtilMessage.message(player, "Clans", "Clan Recruits cannot break blocks.");
            event.setCancelled(true);
        }
    }
}