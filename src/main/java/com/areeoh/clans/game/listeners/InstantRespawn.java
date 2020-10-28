package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class InstantRespawn extends GameModule implements Listener {

    public InstantRespawn(GameManager manager) {
        super(manager, "InstantRespawn");
    }

    @EventHandler
    public void respawn(PlayerDeathEvent event) {
        final Player p = event.getEntity();
        if (p.isInsideVehicle()) {
            Entity mount = p.getVehicle();
            mount.eject();
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            PacketPlayInClientCommand packet = new PacketPlayInClientCommand(
                    PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
            ((CraftPlayer) p).getHandle().playerConnection.a(packet);
            p.setVelocity(new Vector(0, 0, 0));
        }, 2L);
    }
}