package com.areeoh.game.listeners;

import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class DisableCraftingFishingRod extends GameModule implements Listener {

    public DisableCraftingFishingRod(GameManager manager) {
        super(manager, "DisableCraftingFishingRod");
    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        final Material itemType = e.getRecipe().getResult().getType();
        if (itemType == Material.FISHING_ROD) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
