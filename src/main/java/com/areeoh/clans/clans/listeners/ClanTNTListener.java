package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.blockregen.BlockRegenManager;
import com.areeoh.spigot.blockregen.listeners.BlockRegenHandler;
import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.framework.Primitive;
import com.areeoh.spigot.utility.UtilMath;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashSet;
import java.util.Set;

public class ClanTNTListener extends Module<ClanManager> implements Listener {

    private final Set<TNTBlock> materialMap = new HashSet<>();

    public ClanTNTListener(ClanManager manager) {
        super(manager, "ClanTNTListener");
        addPrimitive("TNTExplosionRadius", new Primitive(4.0D));
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (materialMap.isEmpty()) {
            materialMap.add(new TNTBlock(Material.SMOOTH_BRICK, (byte) 0, Material.SMOOTH_BRICK, (byte) 2));
            materialMap.add(new TNTBlock(Material.RED_SANDSTONE, (byte) 2, Material.RED_SANDSTONE, (byte) 0));
            materialMap.add(new TNTBlock(Material.QUARTZ_BLOCK, (byte) 0, Material.QUARTZ_BLOCK, (byte) 1));
            materialMap.add(new TNTBlock(Material.NETHER_BRICK, (byte) 0, Material.NETHERRACK, (byte) 0));
            materialMap.add(new TNTBlock(Material.SANDSTONE, (byte) 2, Material.SANDSTONE, (byte) 0));
            materialMap.add(new TNTBlock(Material.PRISMARINE, (byte) 1, Material.PRISMARINE, (byte) 0));
            materialMap.add(new TNTBlock(Material.PRISMARINE, (byte) 2, Material.PRISMARINE, (byte) 1));
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
            if (clan != null && clan.isTNTProtected()) {
                continue;
            }
            if (getManager(BlockRegenManager.class).getModule(BlockRegenHandler.class).getBlockDataMap().containsKey(block)) {
                continue;
            }
            for (TNTBlock entry : materialMap) {
                if (block.getType() == entry.getFromMaterial() && block.getData() == entry.getFromID()) {
                    block.setTypeIdAndData(entry.getToMaterial().getId(), entry.getToID(), true);
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

    class TNTBlock {

        private final Material fromMaterial;
        private final byte fromID;
        private final Material toMaterial;
        private final byte toID;

        public TNTBlock(Material fromMaterial, byte fromID, Material toMaterial, byte toID) {
            this.fromMaterial = fromMaterial;
            this.fromID = fromID;
            this.toMaterial = toMaterial;
            this.toID = toID;
        }

        public Material getFromMaterial() {
            return fromMaterial;
        }

        public byte getFromID() {
            return fromID;
        }

        public Material getToMaterial() {
            return toMaterial;
        }

        public byte getToID() {
            return toID;
        }
    }
}