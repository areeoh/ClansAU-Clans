package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class WebBreak extends GameModule implements Listener {
    public WebBreak(GameManager manager) {
        super(manager, "WebBreak");
    }

    @EventHandler
    public void WebBreak(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBlock().getType() == Material.WEB) {
            event.setInstaBreak(true);
        }
    }
}