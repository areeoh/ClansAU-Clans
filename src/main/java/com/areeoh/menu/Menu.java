package com.areeoh.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu implements IMenu {

    private final Player player;
    private final String title;
    private final Inventory inventory;
    private List<IButton> buttons;
    private MenuManager menuManager;

    public Menu(MenuManager menuManager, Player player, int size, String title, Button[] buttons) {
        this.menuManager = menuManager;
        this.player = player;
        this.title = title;
        this.buttons = new ArrayList<>(Arrays.asList(buttons));
        this.inventory = Bukkit.createInventory(player, size, title);
        menuManager.getMenus().add(this);
        Construct();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public List<IButton> getButtons() {
        return this.buttons;
    }

    @Override
    public MenuManager getMenuManager() {
        return this.menuManager;
    }

    @Override
    public void Construct() {
        for (IButton button : getButtons()) {
            getInventory().setItem(button.getSlot(), button.getItemStack());
        }
    }

    public boolean isButton(ItemStack item) {
        return getButton(item) != null;
    }

    public IButton getButton(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                return getButtons().stream().filter(button -> button.getItemStack().equals(item)).findFirst().orElse(null);
            }
        }
        return null;
    }

    public void addButton(Button button) {
        getButtons().add(button);
    }
}
