package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class HandleHungerLoss extends GameModule implements Listener {

    public HandleHungerLoss(GameManager manager) {
        super(manager, "HandleHungerLoss");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer() != null) {
            e.getPlayer().setFoodLevel(20);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}