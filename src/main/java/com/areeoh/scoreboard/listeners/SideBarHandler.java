package com.areeoh.scoreboard.listeners;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.events.*;
import com.areeoh.clans.listeners.ClanMovementListener;
import com.areeoh.framework.Module;
import com.areeoh.framework.updater.Update;
import com.areeoh.framework.updater.Updater;
import com.areeoh.scoreboard.ScoreboardManager;
import com.areeoh.scoreboard.ClanScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SideBarHandler extends Module<ClanScoreboardManager> implements Listener, Updater {

    private String header = "ClansAU Season 1";
    private boolean shineDirection = true;
    private int shineIndex = 0;

    public SideBarHandler(ClanScoreboardManager manager) {
        super(manager, "SideBarListener");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateSideBar(event.getPlayer());
    }

    @EventHandler
    public void onClanMove(ClanMoveEvent event) {
        updateSideBar(event.getPlayer(), event.getLocTo());
    }

    @EventHandler
    public void onClanEnergyUpdate(ClanEnergyUpdateEvent event) {
        for (Player online : event.getClan().getOnlinePlayers()) {
            updateSideBar(online);
        }
    }

    @EventHandler
    public void onClanClaim(ClanClaimEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if(entity instanceof Player) {
                updateSideBar((Player) entity);
            }
        }
    }

    @EventHandler
    public void onClaimUnclaim(ClanUnclaimEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if(entity instanceof Player) {
                updateSideBar((Player) entity);
            }
        }
    }

    @EventHandler
    public void onClanCreate(ClanCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanDisband(ClanDisbandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanJoin(ClanJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanLeave(ClanLeaveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanKick(ClanKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player target = Bukkit.getPlayer(event.getTarget().getUUID());
        if (target != null) {
            event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        }
    }

    @EventHandler
    public void onClanAlly(ClanAllyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanTrust(ClanTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanTrustRevoke(ClanRevokeTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanEnemy(ClanEnemyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanNeutral(ClanNeutralEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanPillage(ClanPillageStartEvent event) {
        if(event.isCancelled()) {
            return;
        }
        event.getPillager().getOnlinePlayers().forEach(this::updateSideBar);
        event.getPillagee().getOnlinePlayers().forEach(this::updateSideBar);
    }

    @EventHandler
    public void onClanPillageEnd(ClanPillageEndEvent event) {
        if(event.isCancelled()) {
            return;
        }
        event.getPillager().getOnlinePlayers().forEach(this::updateSideBar);
        event.getPillagee().getOnlinePlayers().forEach(this::updateSideBar);
    }

    public void updateSideBar(Player player) {
        updateSideBar(player, player.getLocation());
    }

    public void updateSideBar(Player player, Location location) {
        final Scoreboard scoreboard = getManager(ScoreboardManager.class).getScoreboard(player);
        Objective objective = scoreboard.getObjective("info");
        if (objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("info", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(getHeaderName());
        final Clan pClan = getManager(ClanManager.class).getClan(player);
        Score cClan = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Clan");
        Score clan = objective.getScore((pClan == null ? "No Clan" : ChatColor.AQUA + pClan.getName()));
        Score blank = objective.getScore(" ");
        Score eEnergy = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Clan Energy");
        Score energy = objective.getScore((pClan == null ? "" : ChatColor.GREEN.toString() + pClan.getEnergyString()));
        Score blank1 = objective.getScore("  ");
        Score cCoins = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Coins");
        //Score coins = objective.getScore(ChatColor.GOLD.toString() + getManager(ClientManager.class).getClient(player.getUniqueId()).getGamer().getCoins());
        Score blank2 = objective.getScore("    ");
        Score tTerritory = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Territory");
        Score territory = objective.getScore(getManager(ClanManager.class).getModule(ClanMovementListener.class).getTerritoryString(player, location));
        if (pClan != null) {
            cClan.setScore(15);
            clan.setScore(14);
            blank.setScore(13);
            eEnergy.setScore(12);
            energy.setScore(11);
            blank1.setScore(10);
            cCoins.setScore(9);
            //coins.setScore(8);
            blank2.setScore(7);
            tTerritory.setScore(6);
            territory.setScore(5);
        } else {
            cClan.setScore(15);
            clan.setScore(14);
            blank.setScore(13);
            cCoins.setScore(12);
            //coins.setScore(11);
            blank2.setScore(10);
            tTerritory.setScore(9);
            territory.setScore(8);
        }

    }

    @Update(ticks = 3)
    public void onUpdate() {
        for (Scoreboard scoreboard : getManager(ScoreboardManager.class).getScoreboardMap().values()) {
            scoreboard.getObjective("info").setDisplayName(getHeaderName());
        }
        this.shineIndex += 1;
        if (this.shineIndex == this.header.length() * 2) {
            this.shineIndex = 0;
            this.shineDirection = (!this.shineDirection);
        }
    }

    public String getHeaderName() {
        String out;
        if (this.shineDirection) {
            out = ChatColor.GOLD + ChatColor.BOLD.toString();
        } else {
            out = ChatColor.WHITE + ChatColor.BOLD.toString();
        }
        for (int i = 0; i < this.header.length(); i++) {
            char c = this.header.charAt(i);
            if (this.shineDirection) {
                if (i == this.shineIndex) {
                    out = out + ChatColor.YELLOW + ChatColor.BOLD.toString();
                }
                if (i == this.shineIndex + 1) {
                    out = out + ChatColor.WHITE + ChatColor.BOLD.toString();
                }
            } else {
                if (i == this.shineIndex) {
                    out = out + ChatColor.YELLOW + ChatColor.BOLD.toString();
                }
                if (i == this.shineIndex + 1) {
                    out = out + ChatColor.GOLD + ChatColor.BOLD.toString();
                }
            }
            out = out + c;
        }
        return out;
    }

    public String getHeader() {
        return header;
    }
}
