package com.areeoh.clans.game.listeners;

import com.areeoh.spigot.core.utility.UtilMath;
import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HandleAnimalDeathDrops extends GameModule implements Listener {

    public HandleAnimalDeathDrops(GameManager manager) {
        super(manager, "HandleAnimalDeathDrops");
    }

    @EventHandler
    public void handleDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        List<ItemStack> drops = event.getDrops();
        if (event.getEntity().getCustomName() == null) {
            if (event.getEntityType() != EntityType.PLAYER) {
                drops.clear();
            }
            if (event.getEntityType() == EntityType.CHICKEN) {
                drops.add(new ItemStack(Material.RAW_CHICKEN, 1));
                drops.add(new ItemStack(Material.FEATHER, 2 + UtilMath.randomInt(1)));
            } else if (event.getEntityType() == EntityType.COW) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.LEATHER, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.MUSHROOM_COW) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.RED_MUSHROOM, 2 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.OCELOT) {
                drops.add(new ItemStack(Material.RAW_FISH, 2 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.PIG) {
                drops.add(new ItemStack(Material.PORK, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SHEEP) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.WOOL, 1 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.VILLAGER) {
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(3)));
            } else if (event.getEntityType() == EntityType.BLAZE) {
                drops.add(new ItemStack(Material.BLAZE_ROD, 1));
                drops.add(new ItemStack(Material.BONE, 6 + UtilMath.randomInt(7)));
            } else if (event.getEntityType() == EntityType.CAVE_SPIDER) {
                drops.add(new ItemStack(Material.WEB, 1));
                drops.add(new ItemStack(Material.STRING, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.SPIDER_EYE, 1));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.CREEPER) {
                drops.add(new ItemStack(Material.COAL, 2 + UtilMath.randomInt(4)));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(7)));
            } else if (event.getEntityType() == EntityType.ENDERMAN) {
                drops.add(new ItemStack(Material.BONE, 12 + UtilMath.randomInt(8)));
            } else if (event.getEntityType() == EntityType.GHAST) {
                drops.add(new ItemStack(Material.GHAST_TEAR, 1));
                drops.add(new ItemStack(Material.BONE, 16 + UtilMath.randomInt(8)));
            } else if (event.getEntityType() == EntityType.IRON_GOLEM) {
                drops.add(new ItemStack(Material.IRON_INGOT, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.BONE, 12 + UtilMath.randomInt(6)));
            } else if (event.getEntityType() == EntityType.MAGMA_CUBE) {
                drops.add(new ItemStack(Material.MAGMA_CREAM, 1));
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
                drops.add(new ItemStack(Material.GRILLED_PORK, 1 + UtilMath.randomInt(2)));
                drops.add(new ItemStack(Material.ROTTEN_FLESH, 1 + UtilMath.randomInt(2)));
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(2)));
                if (UtilMath.randomInt(100) > 90) {
                    drops.add(new ItemStack(Material.GOLD_SWORD, 1));
                }
            } else if (event.getEntityType() == EntityType.SILVERFISH) {
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SKELETON) {
                drops.add(new ItemStack(Material.ARROW, 4 + UtilMath.randomInt(5)));
                drops.add(new ItemStack(Material.BONE, 3 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.SLIME) {
                drops.add(new ItemStack(Material.SLIME_BALL, 1));
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SPIDER) {
                drops.add(new ItemStack(Material.STRING, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.WEB, 1));
                drops.add(new ItemStack(Material.SPIDER_EYE, 1));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.ZOMBIE) {
                event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, 1));
                drops.add(new ItemStack(Material.BONE, 3 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.RABBIT) {
                drops.add(new ItemStack(Material.RABBIT_HIDE, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(3)));
            }
        }
    }
}
