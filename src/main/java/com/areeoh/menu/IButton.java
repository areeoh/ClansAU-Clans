package com.areeoh.menu;

import org.bukkit.inventory.ItemStack;

public interface IButton {

    int getSlot();

    ItemStack getItemStack();

    String getName();

    String[] getDescription();
}