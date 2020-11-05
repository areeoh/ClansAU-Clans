package com.areeoh.clans.game.listeners;

import com.areeoh.spigot.core.utility.UtilItem;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
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
