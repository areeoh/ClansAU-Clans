package com.areeoh.game.items.consumable;

import com.areeoh.framework.interfaces.Rechargeable;
import com.areeoh.framework.interfaces.items.Interactable;
import com.areeoh.utility.ItemBuilder;
import com.areeoh.utility.UtilTime;
import com.areeoh.game.GameManager;
import com.areeoh.game.items.Throwable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class WaterBottle extends Throwable implements Rechargeable, Interactable {

    public WaterBottle(GameManager manager) {
        super(manager, "WaterBottle", ItemBuilder.builder(new ItemStack(Material.POTION))
                .setCustomName(ChatColor.YELLOW + "Water Bottle")
                .addLore("Right click to douse yourself").getItem());
    }

    @Override
    public String getDisplayName() {
        return "Water Bottle";
    }

    @Override
    public Action[] getActions() {
        return new Action[]{Action.RIGHT_CLICK_AIR};
    }

    @Override
    public int getCooldown() {
        return UtilTime.secondsToTicks(6.0F);
    }

    @Override
    public String[] getDesc() {
        return new String[]{
                ChatColor.GRAY + "Right Click: " + ChatColor.YELLOW + "Consume",
                ChatColor.GRAY + "  Instantly restores 50 energy",
                "",
                ChatColor.GRAY + "Cooldown: " + ChatColor.GREEN + "10" + ChatColor.GRAY + " seconds."
        };
    }

    @Override
    public double getStrength() {
        return 0;
    }

    @Override
    public void hit(Entity throwable, Entity entity) {

    }

    @Override
    public void doEffect(Item item) {

    }
}
