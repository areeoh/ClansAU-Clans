package com.areeoh.clans.listeners;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.client.Client;
import com.areeoh.client.ClientManager;
import com.areeoh.framework.Module;
import com.areeoh.utility.UtilFormat;
import com.areeoh.utility.UtilMessage;
import com.areeoh.pillaging.PillageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class ClanInteractListener extends Module<ClanManager> implements Listener {

    private Material[] disallow = new Material[]{Material.CHEST, Material.TRAPPED_CHEST, Material.LEVER, Material.WOOD_BUTTON, Material.STONE_BUTTON, Material.FURNACE, Material.FENCE_GATE, Material.WORKBENCH, Material.DISPENSER, Material.BED, Material.WORKBENCH, Material.BURNING_FURNACE, Material.WOODEN_DOOR, Material.WOOD_DOOR, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.TRAP_DOOR, Material.BREWING_STAND, Material.BREWING_STAND_ITEM};

    public ClanInteractListener(ClanManager manager) {
        super(manager, "ClanInteractListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final Clan lClan = getManager().getClan(block.getLocation());
        if (lClan == null) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (!lClan.equals(clan)) {
            if (getManager(PillageManager.class).isPillaging(clan, lClan)) {
                return;
            }
            final Client client = getManager(ClientManager.class).getClient(player.getUniqueId());
            if (client.isAdministrating()) {
                return;
            }
            if (!Arrays.asList(disallow).contains(block.getType())) {
                return;
            }
            final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, lClan);
            UtilMessage.message(player, "Clans", "You cannot use " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + " in " + clanRelation.getSuffix() + (lClan.isAdmin() ? "" : "Clan ") + lClan.getName() + ChatColor.GRAY + ".");
            event.setCancelled(true);
        } else if (clan.getMemberRole(player.getUniqueId()) == Clan.MemberRole.RECRUIT) {
            if (!(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) {
                return;
            }
            UtilMessage.message(player, "Clans", "Clan Recruits cannot access " + ChatColor.GREEN +
                    UtilFormat.cleanString(block.getType().toString()) + ChatColor.GRAY + ".");
            event.setCancelled(true);
        }
    }
}
