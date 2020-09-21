package com.areeoh.fishing.listeners;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.framework.Module;
import com.areeoh.utility.*;
import com.areeoh.fishing.Fish;
import com.areeoh.fishing.FishingManager;
import com.areeoh.fishing.events.CustomFishingEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishingListener extends Module<FishingManager> implements Listener {

    private final EntityType[] entityTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.GHAST};

    public FishingListener(FishingManager manager) {
        super(manager, "FishingHandler");
    }

    @EventHandler
    public void onFishingEvent(PlayerFishEvent event) {
        final Entity caught = event.getCaught();
        if (caught == null) {
            return;
        }
        if (caught.getType() != EntityType.DROPPED_ITEM) {
            return;
        }
        event.setExpToDrop(0);
        caught.remove();
        Bukkit.getPluginManager().callEvent(new CustomFishingEvent(event.getPlayer(), caught));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCancelFishing(CustomFishingEvent event) {
        final Entity caught = event.getCaught();
        final Clan clan = getManager(ClanManager.class).getClan(caught.getLocation());
        final Player player = event.getPlayer();
        if (clan == null || (!(clan.getName().equalsIgnoreCase("Fields") || clan.getName().equalsIgnoreCase("Lake")))) {
            event.setCancelled(true);
            UtilMessage.message(player, "Fishing", "You can only fish at " + ChatColor.WHITE + "Lake" + ChatColor.GRAY + " located at " + ChatColor.YELLOW + "(0x, 0z)" + ChatColor.GRAY + ".");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomFishing(CustomFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Fish fish = Fish.values()[UtilMath.randomInt(0, Fish.values().length - 1)];
        final Player player = event.getPlayer();

        RandomGaussian gaussian = new RandomGaussian();

        double mean = 550.0F;
        double variance = 170.0f;
        int randVal = UtilMath.randomInt(0, 500);

        if (randVal < 500 * 0.15) {
            final LivingEntity entity = (LivingEntity) event.getCaught().getWorld().spawnEntity(event.getCaught().getLocation(), entityTypes[UtilMath.randomInt(0, entityTypes.length - 1)]);
            entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(-1.0D).normalize());
            UtilMessage.message(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(entity.getType().name()) + ChatColor.GRAY + ".");
            return;
        }
        CatchType catchType = CatchType.STANDARD;

        if (randVal > 500 * 0.75) {
            mean = 1000;
            variance = 150;
            catchType = CatchType.BIG_CATCH;
        } else if (randVal > 500 * 0.9) {
            mean = 1800;
            variance = 300;
            catchType = CatchType.HUGE_CATCH;
        }

        //TODO ADD DROPPED ITEMS LIKE PICKAXE ETC

        final int i = gaussian.getGaussian(mean, variance);

        final ItemStack itemStack = UtilItem.createItem(Material.RAW_FISH, i / 3, ChatColor.YELLOW + UtilFormat.cleanString(fish.name()), new String[0]);
        ;
        UtilItem.insert(player, itemStack);

        catchType.Broadcast(getManager(ClanManager.class), player, fish, i);
    }

    private enum CatchType {
        BIG_CATCH("Big Catch"),
        HUGE_CATCH("Huge Catch"),
        STANDARD("Fishing");

        String prefix;

        CatchType(String prefix) {
            this.prefix = prefix;
        }

        public void Broadcast(ClanManager clanManager, Player player, Fish fish, int size) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (this == STANDARD && player != online) {
                    continue;
                }
                final ClanManager.ClanRelation relation = clanManager.getClanRelation(clanManager.getClan(player), clanManager.getClan(online));
                UtilMessage.message(online, prefix, relation.getSuffix() + player.getName() + ChatColor.GRAY + " has caught a " + ChatColor.GREEN + size + " Pound " + fish.getFormattedName() + ChatColor.GRAY + ".");
            }
        }
    }
}