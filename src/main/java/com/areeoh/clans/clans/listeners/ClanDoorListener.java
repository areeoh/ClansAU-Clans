package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.recharge.RechargeManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClanDoorListener extends Module<ClanManager> implements Listener {

    public ClanDoorListener(ClanManager manager) {
        super(manager, "ClanDoorListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if(block.getTypeId() == 71 || block.getTypeId() == 167) {
            final Player player = event.getPlayer();
            if (getManager().hasAccess(player, block.getLocation())) {
                if (block.getTypeId() == 71) {
                    if (block.getData() >= 8) {
                        block = block.getRelative(BlockFace.DOWN);
                    }
                }
                if (block.getData() < 4) {
                    block.setData((byte) (block.getData() + 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
                } else {
                    block.setData((byte) (block.getData() - 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 1.0F);
                }
                EntityPlayer ep = ((CraftPlayer) player).getHandle();
                PacketPlayOutAnimation packetPlayOutAnimation = new PacketPlayOutAnimation(ep, 0);
                (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(packetPlayOutAnimation);
            } else {
                if(!getManager(RechargeManager.class).use(player, "Door Knock", 350L, false, false)) {
                    return;
                }
                block.getWorld().playEffect(block.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
                EntityPlayer ep = ((CraftPlayer)event.getPlayer()).getHandle();
                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                (((CraftPlayer)event.getPlayer()).getHandle()).playerConnection.sendPacket(packet);
            }
        }
    }
}
