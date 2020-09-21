package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class DisableBlockBurn extends GameModule implements Listener {

    public DisableBlockBurn(GameManager manager) {
        super(manager, "DisableBlockBurn");
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
}