package com.areeoh.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface IMenu {

    Player getPlayer();

    String getTitle();

    Inventory getInventory();

    List<IButton> getButtons();

    MenuManager getMenuManager();

    void Construct();

}