package com.areeoh.game.listeners;

import com.areeoh.clans.ClanManager;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class ClanPiston extends GameModule implements Listener {

    public ClanPiston(GameManager manager) {
        super(manager, "ClanPiston");
    }

    @EventHandler
    public void onPistonEvent(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (((getManager(ClanManager.class).getClan(b.getLocation().getChunk()) != null) && (getManager(ClanManager.class).getClan(e.getBlock().getLocation().getChunk()) == null)) ||
                    (getManager(ClanManager.class).getClan(e.getBlock().getLocation().getChunk()) != getManager(ClanManager.class).getClan(b.getLocation().getChunk()))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonEvent(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (((getManager(ClanManager.class).getClan(b.getLocation().getChunk()) != null) && (getManager(ClanManager.class).getClan(e.getBlock().getLocation().getChunk()) == null)) || (getManager(ClanManager.class).getClan(e.getBlock().getLocation().getChunk()) != getManager(ClanManager.class).getClan(b.getLocation().getChunk()))) {
                e.setCancelled(true);
            }
        }
    }
}