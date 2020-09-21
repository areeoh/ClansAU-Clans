package com.areeoh.menu;

import com.areeoh.ClansAUCore;
import com.areeoh.framework.Manager;
import com.areeoh.framework.Module;
import com.areeoh.menu.listeners.MenuHandler;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MenuManager extends Manager<Module> {

    private List<IMenu> menus;

    public MenuManager(ClansAUCore plugin) {
        super(plugin, "MenuManager");
        this.menus = new ArrayList<>();
    }

    @Override
    public void registerModules() {
        addModule(new MenuHandler(this));
    }

    public List<IMenu> getMenus() {
        return menus;
    }

    public boolean isMenu(Inventory inventory) {
        return menus.stream().anyMatch(menu -> menu.getTitle().equalsIgnoreCase(inventory.getName()));
    }

    public Menu getMenu(Inventory inventory) {
        return (Menu) menus.stream().filter(menu -> menu.getTitle().equals(inventory.getName())).filter(menu -> inventory.getViewers().size() > 0).filter(menu -> menu.getPlayer().getUniqueId().equals(inventory.getViewers().get(0).getUniqueId())).findFirst().orElse(null);
    }
}