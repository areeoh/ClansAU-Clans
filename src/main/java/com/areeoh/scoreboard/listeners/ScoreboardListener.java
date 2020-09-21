package com.areeoh.scoreboard.listeners;

import com.areeoh.clans.events.*;
import com.areeoh.framework.Module;
import com.areeoh.scoreboard.ClanScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardListener extends Module<ClanScoreboardManager> implements Listener {

    public ScoreboardListener(ClanScoreboardManager manager) {
        super(manager, "ScoreboardListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        getManager().addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanCreate(ClanCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanDisband(ClanDisbandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).removeClan(event.getClan());
    }

    @EventHandler
    public void onClanJoin(ClanJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanLeave(ClanLeaveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanKick(ClanKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player target = Bukkit.getPlayer(event.getTarget().getUUID());
        if (target != null) {
            getManager(ClanScoreboardManager.class).removePlayer(target);
        }
    }

    @EventHandler
    public void onClanAlly(ClanAllyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getClan());
        getManager(ClanScoreboardManager.class).updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanTrust(ClanTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getClan());
        getManager(ClanScoreboardManager.class).updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanTrustRevoke(ClanRevokeTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getClan());
        getManager(ClanScoreboardManager.class).updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanEnemy(ClanEnemyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getClan());
        getManager(ClanScoreboardManager.class).updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanNeutral(ClanNeutralEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getClan());
        getManager(ClanScoreboardManager.class).updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanPillage(ClanPillageStartEvent event) {
        if(event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getPillager());
        getManager(ClanScoreboardManager.class).updateRelation(event.getPillagee());
    }

    @EventHandler
    public void onClanPillageEnd(ClanPillageEndEvent event) {
        if(event.isCancelled()) {
            return;
        }
        getManager(ClanScoreboardManager.class).updateRelation(event.getPillager());
        getManager(ClanScoreboardManager.class).updateRelation(event.getPillagee());
    }
}