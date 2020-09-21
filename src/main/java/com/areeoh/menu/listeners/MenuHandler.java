package com.areeoh.menu.listeners;

import com.areeoh.framework.Module;
import com.areeoh.recharge.RechargeManager;
import com.areeoh.menu.Button;
import com.areeoh.menu.Menu;
import com.areeoh.menu.MenuManager;
import com.areeoh.menu.events.ButtonClickEvent;
import com.areeoh.menu.events.MenuCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuHandler extends Module<MenuManager> implements Listener {

    public MenuHandler(MenuManager manager) {
        super(manager, "MenuHandler");
    }

    @EventHandler
    public void onButtonClick(InventoryClickEvent event) {
        if(event.getInventory() == null) return;
        if(event.getCurrentItem() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            if (getManager().isMenu(event.getInventory())) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                Menu menu = getManager().getMenu(event.getClickedInventory());
                if (menu.isButton(event.getCurrentItem())) {
                    Button button = (Button) menu.getButton(event.getCurrentItem());
                    if (getManager(RechargeManager.class).use(player, "Button Click", 250L, false, false)) {
                        Bukkit.getPluginManager().callEvent(new ButtonClickEvent(player, menu, button, event.getClick(), event.getSlot()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (getManager().isMenu(event.getInventory())) {
                Player player = (Player) event.getPlayer();
                Menu menu = getManager().getMenu(event.getView().getTopInventory());
                Bukkit.getPluginManager().callEvent(new MenuCloseEvent(player, menu));
            }
        }
    }

    @EventHandler
    public void onMenuClose(MenuCloseEvent event) {
        if (event.getMenu() != null) {
            getManager().getMenus().remove(event.getMenu());
        }
    }
}