package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.events.ClanMoveEvent;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.core.framework.Module;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class ClanGamemodeListener extends Module<ClanManager> implements Listener {

    public ClanGamemodeListener(ClanManager manager) {
        super(manager, "ClanGamemodeListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanMove(ClanMoveEvent event) {
        handleGamemode(event.getPlayer(), event.getLocTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        handleGamemode(event.getPlayer(), event.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        handleGamemode(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        handleGamemode(event.getPlayer(), event.getRespawnLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(event.getEntered() instanceof Player) {
            handleGamemode(((Player) event.getEntered()).getPlayer(), event.getEntered().getLocation());
        }
    }

    private void handleGamemode(Player player, Location location) {
        final Clan clanTo = getManager().getClan(location);
        final Clan clan = getManager().getClan(player.getUniqueId());
        final ClanManager.ClanRelation clanRelation = getManager().getClanRelation(clan, clanTo);
        if(clanRelation == ClanManager.ClanRelation.PILLAGE) {
            if(player.getGameMode() == GameMode.ADVENTURE) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            return;
        }
        if(clanTo == null || clanTo.getName().equalsIgnoreCase("Fields")) {
            if(player.getGameMode() == GameMode.ADVENTURE) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            return;
        }
        if(!clanTo.equals(clan) && !clanTo.getName().equalsIgnoreCase("Fields")) {
            if(player.getGameMode() == GameMode.SURVIVAL) {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }
    }
}
