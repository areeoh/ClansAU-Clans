package com.areeoh.game.listeners;

import com.areeoh.utility.UtilItem;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class HandleItemNameChange extends GameModule implements Listener {
    public HandleItemNameChange(GameManager manager) {
        super(manager, "HandleItemNameChange");
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        UtilItem.updateNames(e.getItem().getItemStack());
    }
}
