package com.areeoh.clans.game.items;

import com.areeoh.clans.game.GameManager;
import com.areeoh.core.utility.UtilItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public abstract class Throwable extends GameItem {

    public Throwable(GameManager manager, String moduleName, ItemStack item) {
        super(manager, moduleName, item);
    }

    public Item throwItem(LivingEntity entity, double strength) {
        Item dropItem = entity.getWorld().dropItem(entity.getEyeLocation().subtract(0.0, 0.25, 0.0), getItem());
        dropItem.setPickupDelay(9999);
        // dropItem.setInvulnerable(true);

        dropItem.setVelocity(entity.getEyeLocation().getDirection().normalize().multiply(strength));
        //dropItem.setVelocity(entity.getEyeLocation().add(entity.getEyeLocation().getDirection().multiply(strength)).getDirection().multiply(1.0F).add(new Vector(0.0, 0.4, 0.0)));
        UtilItem.appendMeta(getPlugin(), dropItem, "isThrowable", "UUID:" + entity.getUniqueId().toString());
        return dropItem;
    }

    public abstract double getStrength();

    public abstract void hit(Entity throwable, Entity entity);

    public abstract void doEffect(Item item);

}
