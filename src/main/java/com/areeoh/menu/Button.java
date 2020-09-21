package com.areeoh.menu;

import com.areeoh.utility.UtilItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Button implements IButton {

    private final int slot;
    private final ItemStack item;
    private final String name;
    private final String[] lore;

    public Button(int slot, ItemStack item, String name, String[] lore) {
        this.slot = slot;
        this.name = name;
        this.lore = lore;
        this.item = UtilItem.removeAttributes(UtilItem.createItem(item.getType(), item.getAmount(), name, lore));
    }

    public Button(int slot, ItemStack item, int amount, byte itemID, String name, String[] lore, boolean addGlow) {
        this.slot = slot;
        this.name = name;
        this.lore = lore;
        this.item = UtilItem.removeAttributes(UtilItem.createItem(item.getType(), amount, itemID, name, lore, addGlow));
    }

    public Button(int slot, ItemStack item, String name, List<String> lore) {
        this.lore = lore.toArray(new String[0]);
        this.slot = slot;
        this.name = name;
        this.item = UtilItem.removeAttributes(UtilItem.setItemNameAndLore(item, name, lore));
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDescription() {
        return lore;
    }
}
