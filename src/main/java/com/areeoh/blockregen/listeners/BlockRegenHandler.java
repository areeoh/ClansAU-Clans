package com.areeoh.blockregen.listeners;

import com.areeoh.blockregen.BlockRegenManager;
import com.areeoh.blockregen.data.BlockRegenData;
import com.areeoh.framework.Module;
import com.areeoh.framework.updater.Update;
import com.areeoh.framework.updater.Updater;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockRegenHandler extends Module<BlockRegenManager> implements Listener, Updater {

    private HashMap<Block, BlockRegenData> blockDataMap;

    public BlockRegenHandler(BlockRegenManager manager) {
        super(manager, "BlockRegenListener");
        this.blockDataMap = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (blockDataMap.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PistonExtend(BlockPistonExtendEvent event) {
        Block eventBlock = event.getBlock();
        for (int i = 0; i <= 12; i++) {
            eventBlock = eventBlock.getRelative(event.getDirection());
            if (eventBlock.getType() == Material.AIR) {
                return;
            }
            if (blockDataMap.containsKey(eventBlock)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PistonRetract(BlockPistonRetractEvent event) {
        Block eventBlock = event.getBlock();
        for (int i = 0; i < 2; i++) {
            eventBlock = eventBlock.getRelative(event.getDirection().getOppositeFace());
            if (blockDataMap.containsKey(eventBlock)) {
                event.setCancelled(true);
            }
        }
    }


    @Override
    public void shutdown() {
        if (blockDataMap.isEmpty()) {
            return;
        }
        for (Iterator<Map.Entry<Block, BlockRegenData>> it = blockDataMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Block, BlockRegenData> entry = it.next();
            if (entry.getKey().getType() == Material.CHEST) {
                entry.getKey().breakNaturally();
            }
            entry.getKey().setTypeIdAndData(entry.getValue().getOldMaterial().getId(), entry.getValue().getOldMaterialID(), true);
            it.remove();
        }
    }

    public void addBlock(Block block, Material newMaterial, byte newMaterialID, long timeLength) {
        blockDataMap.put(block, new BlockRegenData(block, newMaterial, newMaterialID, timeLength));
    }

    public void RegenBlock(Block block) {
        RegenBlock(block.getLocation());
    }

    public void RegenBlock(Location location) {
        if (blockDataMap.isEmpty()) {
            return;
        }
        for (Iterator<Map.Entry<Block, BlockRegenData>> it = blockDataMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Block, BlockRegenData> data = it.next();
            if (data.getKey().getLocation().equals(location)) {
                data.getKey().setTypeIdAndData(data.getValue().getOldMaterial().getId(), data.getValue().getOldMaterialID(), true);
                it.remove();
            }
        }
    }

    @Update(ticks = 1)
    public void onUpdate() {
        if (blockDataMap.isEmpty()) {
            return;
        }
        for (Iterator<Map.Entry<Block, BlockRegenData>> it = blockDataMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Block, BlockRegenData> entry = it.next();
            if (entry.getValue().getSysTime() + entry.getValue().getTimeLength() < System.currentTimeMillis()) {
                if (entry.getKey().getType() == Material.CHEST) {
                    entry.getKey().breakNaturally();
                }
                entry.getKey().setTypeIdAndData(entry.getValue().getOldMaterial().getId(), entry.getValue().getOldMaterialID(), true);
                it.remove();
            }
        }
    }

    public HashMap<Block, BlockRegenData> getBlockDataMap() {
        return blockDataMap;
    }
}