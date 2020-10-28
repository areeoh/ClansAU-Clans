package com.areeoh.clans.game.items;

import com.areeoh.clans.game.GameManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.framework.interfaces.items.Interactable;
import com.areeoh.core.recharge.RechargeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class GameItem extends Module<GameManager> {

    private ItemStack item;

    private boolean override;
    private boolean consumable;
    private boolean durability;

    public GameItem(GameManager manager, String moduleName, ItemStack item) {
        super(manager, moduleName);

        override = false;
        consumable = false;
        durability = false;

        this.item = item;
    }

    public abstract String getDisplayName();

    public abstract int getCooldown();

    public abstract String[] getDesc();

    public ItemStack getItem() {
        return this.item;
    }

    protected boolean isItem(ItemStack itemStack) {
        return this.item.isSimilar(itemStack);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (this instanceof Interactable && !Arrays.asList(((Interactable) this).getActions()).contains(event.getAction())) {
            return;
        }
        if (!isItem(player.getItemInHand())) {
            return;
        }
        if (!getManager(RechargeManager.class).use(player, getDisplayName(), getCooldown(), true, true)) {
            return;
        }
        if (this instanceof Throwable) {
            ((Throwable) this).throwItem(player, ((Throwable) this).getStrength());
        }

        // if (getManager().getModules(Throwable.class).stream().)
    }
}
