package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.blockregen.BlockRegenManager;
import com.areeoh.core.blockregen.listeners.BlockRegenHandler;
import com.areeoh.core.framework.Module;
import com.areeoh.core.framework.Primitive;
import com.areeoh.core.utility.UtilMath;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class ClanTNTListener extends Module<ClanManager> implements Listener {

    private Map<Pair<Material, Byte>, Pair<Material, Byte>> materialMap = new HashMap<>();
    private Map<Location, UUID> tntMap = new HashMap<>();

    public ClanTNTListener(ClanManager manager) {
        super(manager, "ClanTNTListener");
        addPrimitive("TNTExplosionRadius", new Primitive(4.0D, 4.0D));
    }

    @EventHandler
    public void onTNTPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBlock().getType() != Material.TNT) {
            return;
        }
        tntMap.put(event.getBlock().getLocation(), event.getPlayer().getUniqueId());

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (materialMap.isEmpty()) {
            materialMap.put(new Pair<>(Material.SMOOTH_BRICK, (byte) 0), new Pair<>(Material.SMOOTH_BRICK, (byte) 2));
            materialMap.put(new Pair<>(Material.RED_SANDSTONE, (byte) 2), new Pair<>(Material.RED_SANDSTONE, (byte) 0));
            materialMap.put(new Pair<>(Material.QUARTZ_BLOCK, (byte) 0), new Pair<>(Material.QUARTZ_BLOCK, (byte) 1));
            materialMap.put(new Pair<>(Material.NETHER_BRICK, (byte) 0), new Pair<>(Material.NETHERRACK, (byte) 0));
            materialMap.put(new Pair<>(Material.SANDSTONE, (byte) 2), new Pair<>(Material.SANDSTONE, (byte) 0));
            materialMap.put(new Pair<>(Material.PRISMARINE, (byte) 1), new Pair<>(Material.PRISMARINE, (byte) 0));
            materialMap.put(new Pair<>(Material.PRISMARINE, (byte) 2), new Pair<>(Material.PRISMARINE, (byte) 1));
        }
        if (event.isCancelled()) {
            return;
        }
        HashSet<Clan> clans = new HashSet<>();
        if (event.getEntity() == null || (!(event.getEntity() instanceof TNTPrimed))) {
            return;
        }
        event.setCancelled(true);
        bLoop:
        for (Block block : UtilMath.getInRadius(event.getLocation(), getPrimitiveCasted(Double.class, "TNTExplosionRadius"), getPrimitiveCasted(Double.class, "TNTExplosionRadius")).keySet()) {
            if (block.getType() == Material.BEDROCK || block.getType() == Material.OBSIDIAN || block.getType() == Material.BARRIER) {
                continue;
            }
            if (block.isLiquid()) {
                block.setType(Material.AIR);
            }
            final Clan clan = getManager(ClanManager.class).getClan(block.getLocation());
            if (clan != null && clan.isAdmin()) {
                continue;
            }
            //if (clan != null && clan.isTNTProtected()) {
            //continue;
            //}
            if (getManager(BlockRegenManager.class).getModule(BlockRegenHandler.class).getBlockDataMap().containsKey(block)) {
                continue;
            }
            for (Map.Entry<Pair<Material, Byte>, Pair<Material, Byte>> entry : materialMap.entrySet()) {
                if (block.getType() == entry.getKey().getKey() && block.getData() == entry.getKey().getValue()) {
                    block.setTypeIdAndData(entry.getValue().getKey().getId(), entry.getValue().getValue(), true);
                    continue bLoop;
                }
            }
            block.breakNaturally();
            if (clan != null) {
                clans.add(clan);
            }
        }
        TNTPrimed tntPrimed = (TNTPrimed) event.getEntity();
        final Entity source = tntPrimed.getSource();
        if (source instanceof Player) {
            final Player playerSource = (Player) source;
            final Clan attacker = getManager(ClanManager.class).getClan(playerSource);
            for (Clan clan : clans) {
                final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, attacker);
                if (attacker == null) {
                    clan.inform(true, "Clans", "Your Territory is under attack by " + clanRelation.getSuffix() + playerSource.getName() + ChatColor.GRAY + ChatColor.GRAY + ".");
                } else {
                    clan.inform(true, "Clans", "Your Territory is under attack by " + clanRelation.getSuffix() + "Clan " + attacker.getName() + ChatColor.GRAY + ChatColor.GRAY + ".");
                }
                clan.playSound(Sound.NOTE_PLING, 1.0F, 1.0F);
            }
        }
    }
}
