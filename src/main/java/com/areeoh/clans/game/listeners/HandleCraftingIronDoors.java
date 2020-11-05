package com.areeoh.clans.game.listeners;

import com.areeoh.spigot.core.utility.UtilItem;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class HandleCraftingIronDoors extends GameModule implements Listener {

    public HandleCraftingIronDoors(GameManager manager) {
        super(manager, "HandleCraftingIronDoors");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void IronDoor(PrepareItemCraftEvent event) {
        if (event.getRecipe().getResult() == null) {
            return;
        }
        Material type = event.getRecipe().getResult().getType();
        if (type != Material.WOOD_DOOR &&
                type != Material.SPRUCE_DOOR_ITEM &&
                type != Material.JUNGLE_DOOR_ITEM &&
                type != Material.BIRCH_DOOR_ITEM &&
                type != Material.ACACIA_DOOR_ITEM &&
                type != Material.DARK_OAK_DOOR_ITEM) {
            return;
        }
        if (!(event.getInventory() instanceof CraftInventory)) {
            return;
        }
        CraftingInventory inv = event.getInventory();
        inv.setResult(UtilItem.updateNames(new ItemStack(Material.IRON_DOOR, 2)));
    }
}
