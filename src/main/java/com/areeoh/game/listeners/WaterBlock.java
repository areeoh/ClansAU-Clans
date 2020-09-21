package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaterBlock extends GameModule implements Listener {

    public WaterBlock(GameManager manager) {
        super(manager, "WaterBlock");
    }

    @EventHandler
    public void LapisPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.LAPIS_BLOCK) {
            return;
        }
        event.getBlock().setTypeId(8);
        event.getBlock().getState().update();
        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, 8);
        event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.SPLASH, 2f, 1f);
    }
}
