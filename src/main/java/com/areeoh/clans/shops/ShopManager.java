package com.areeoh.clans.shops;


import com.areeoh.clans.shops.npc.ShopSkeleton;
import com.areeoh.clans.shops.npc.ShopVillager;
import com.areeoh.clans.shops.npc.ShopZombie;
import com.areeoh.spigot.framework.Manager;
import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.utility.UtilEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class ShopManager extends Manager<Module> {

    public ShopManager(Plugin plugin) {
        super(plugin, "ShopManager");
    }

    @Override
    public void onEnable() {
        UtilEntity.registerEntity(54, ShopZombie.class);
        UtilEntity.registerEntity(51, ShopSkeleton.class);
        UtilEntity.registerEntity(120, ShopVillager.class);
    }

    @Override
    public void registerModules() {
        addModule(new Shop(this, "Farmer", new ShopVillager(((CraftWorld)Bukkit.getWorld("world")).getHandle())));
    }
}
