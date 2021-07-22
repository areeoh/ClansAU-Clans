package com.areeoh.clans.shops;

import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.repository.RepositoryManager;
import net.minecraft.server.v1_8_R3.EntityCreature;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Shop extends Module<ShopManager> {

    private final EntityCreature entity;

    public Shop(ShopManager manager, String moduleName, EntityCreature entity) {
        super(manager, moduleName);
        this.entity = entity;
    }

    public void saveShop() {
        getManager(RepositoryManager.class).getModule(ShopRepository.class).saveShop(this);
    }

    public EntityCreature getEntity() {
        return entity;
    }

    public CraftEntity spawn(Location location) {
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entity.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity.getBukkitEntity();
    }
}