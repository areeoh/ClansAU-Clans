package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.recharge.RechargeManager;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpongeBounce extends GameModule implements Listener {
    public SpongeBounce(GameManager manager) {
        super(manager, "SpongeBounce");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.SPONGE) {
            return;
        }
        if (!player.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(event.getClickedBlock())) {
            return;
        }
        if (!getManager(RechargeManager.class).use(player, "Sponge", 800L, false, false)) {
            return;
        }
        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
        for (int i = 0; i < 3; i++) {
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19);
        }
        player.setVelocity(new Vector(0.0D, 1.6D, 0.0D));
        event.setCancelled(true);
        player.setFallDistance(0.0F);
    }
}
