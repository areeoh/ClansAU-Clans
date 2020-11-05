package com.areeoh.clans.scoreboard;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.*;
import com.areeoh.clans.clans.listeners.ClanMovementListener;
import com.areeoh.spigot.core.client.Client;
import com.areeoh.spigot.core.client.ClientManager;
import com.areeoh.spigot.core.client.Rank;
import com.areeoh.spigot.core.client.events.ClientJoinEvent;
import com.areeoh.spigot.core.scoreboard.ScoreboardManager;
import com.areeoh.spigot.core.scoreboard.ScoreboardPriority;
import com.areeoh.spigot.core.scoreboard.data.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class ClanScoreboard extends PlayerScoreboard implements Listener {

    private final String noneTeam = "@None";

    public ClanScoreboard(ScoreboardManager manager) {
        super(manager, "ClanScoreboard", "  ClansAU Season 1  ");
    }

    @Override
    public void giveNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        addPlayer(player);
    }

    public void updateSideBar(Player player) {
        updateSideBar(player, player.getLocation());
    }

    public void updateSideBar(Player player, Location location) {
        final Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("info");
        if (objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("info", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(getDisplayName());
        final Clan pClan = getManager(ClanManager.class).getClan(player);
        Score cClan = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Clan");
        Score clan = objective.getScore((pClan == null ? "No Clan" : ChatColor.AQUA + pClan.getName()));
        Score blank = objective.getScore(getBlank(1));
        Score eEnergy = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Clan Energy");
        Score energy = objective.getScore((pClan == null ? "" : ChatColor.GREEN.toString() + pClan.getEnergyString()));
        Score blank1 = objective.getScore(getBlank(2));
        Score cCoins = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Coins");
        //Score coins = objective.getScore(ChatColor.GOLD.toString() + getManager(ClientManager.class).getClient(player.getUniqueId()).getGamer().getCoins());
        Score blank2 = objective.getScore(getBlank(4));
        Score tTerritory = objective.getScore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Territory");
        Score territory = objective.getScore(getManager(ClanManager.class).getModule(ClanMovementListener.class).getTerritoryString(player, location));
        cClan.setScore(15);
        clan.setScore(14);
        blank.setScore(13);
        if (pClan != null) {
            eEnergy.setScore(12);
            energy.setScore(11);
            blank1.setScore(10);
            cCoins.setScore(9);
            //coins.setScore(8);
            blank2.setScore(7);
            tTerritory.setScore(6);
            territory.setScore(5);
        } else {
            cCoins.setScore(12);
            //coins.setScore(11);
            blank2.setScore(10);
            tTerritory.setScore(9);
            territory.setScore(8);
        }
    }

    @EventHandler
    public void onClientJoinEvent(ClientJoinEvent event) {
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
            if (entity instanceof Player) {
                updateSideBar((Player) entity);
            }
        }
    }

    @EventHandler
    public void onClaimUnclaim(ClanUnclaimEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof Player) {
                updateSideBar((Player) entity);
            }
        }
    }

    @EventHandler
    public void onClanCreate(ClanCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }
        for (Player online : event.getClan().getOnlinePlayers()) {
            updateSideBar(online);
        }
        addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanDisband(ClanDisbandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        removeClan(event.getClan());
    }

    @EventHandler
    public void onClanJoin(ClanJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanLeave(ClanLeaveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onClanKick(ClanKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player target = Bukkit.getPlayer(event.getTarget().getUUID());
        if (target != null) {
            event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
            removePlayer(target);
        }
    }

    @EventHandler
    public void onClanAlly(ClanAllyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getClan());
        updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanTrust(ClanTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getClan());
        updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanTrustRevoke(ClanRevokeTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getClan());
        updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanEnemy(ClanEnemyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getClan());
        updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanNeutral(ClanNeutralEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getClan().getOnlinePlayers().forEach(this::updateSideBar);
        event.getOther().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getClan());
        updateRelation(event.getOther());
    }

    @EventHandler
    public void onClanPillage(ClanPillageStartEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getPillager().getOnlinePlayers().forEach(this::updateSideBar);
        event.getPillagee().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getPillager());
        updateRelation(event.getPillagee());
    }

    @EventHandler
    public void onClanPillageEnd(ClanPillageEndEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.getPillager().getOnlinePlayers().forEach(this::updateSideBar);
        event.getPillagee().getOnlinePlayers().forEach(this::updateSideBar);
        updateRelation(event.getPillager());
        updateRelation(event.getPillagee());
    }

    private void addPlayer(Player player) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        for (Player online : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = online.getScoreboard();
            if (clan == null) {
                if (scoreboard.getTeam(noneTeam) == null) {
                    scoreboard.registerNewTeam(noneTeam);
                }
                scoreboard.getTeam(noneTeam).setPrefix(ChatColor.YELLOW + "");
                scoreboard.getTeam(noneTeam).addPlayer(player);
            } else {
                addClan(scoreboard, clan, getManager(ClanManager.class).getClan(online));
                scoreboard.getTeam(clan.getName()).addPlayer(player);
            }
        }
        final Scoreboard scoreboard = player.getScoreboard();
        scoreboard.getTeams().forEach(Team::unregister);
        for (Clan c : getManager(ClanManager.class).getClanSet()) {
            if (c.isOnline()) {
                addClan(scoreboard, c, getManager(ClanManager.class).getClan(player.getUniqueId()));
                Team team = scoreboard.getTeam(c.getName());
                for (UUID member : c.getMemberMap().keySet()) {
                    Player p = Bukkit.getPlayer(member);
                    if (p != null) {
                        team.addPlayer(p);
                    }
                }
            }
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (getManager(ClanManager.class).getClan(online.getUniqueId()) == null) {
                if (getManager(ClientManager.class).getOnlineClient(online.getUniqueId()).hasRank(Rank.MEDIA, false)) {
                    updateRank(online);
                    continue;
                }
                if (scoreboard.getTeam(noneTeam) == null) {
                    scoreboard.registerNewTeam(noneTeam);
                }
                scoreboard.getTeam(noneTeam).setPrefix(ChatColor.YELLOW + "");
                scoreboard.getTeam(noneTeam).addPlayer(online);
            }
        }
    }

    private void updateRank(Player player) {
        final Client client = getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client.hasRank(Rank.MEDIA, false)) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = online.getScoreboard();
                String str = getTrimmedRankPrefix(client.getRank());
                if (scoreboard.getTeam(str) == null) {
                    scoreboard.registerNewTeam(str);
                    scoreboard.getTeam(str).setPrefix(client.getRank().getRankColor(true) + " " + ChatColor.YELLOW);
                }
                scoreboard.getTeam(str).addPlayer(player);
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = online.getScoreboard();
                if (scoreboard.getTeam(noneTeam) == null) {
                    scoreboard.registerNewTeam(noneTeam);
                    scoreboard.getTeam(noneTeam).setPrefix(ChatColor.YELLOW.toString());
                }
                scoreboard.getTeam(noneTeam).addPlayer(player);
            }
        }
    }

    private void updateRelation(Clan clan) {
        for (UUID uuid : clan.getMemberMap().keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            for (Team team : player.getScoreboard().getTeams()) {
                if (team.getName().equals(noneTeam)) {
                    team.setPrefix(ChatColor.YELLOW.toString());
                } else {
                    Clan other = getManager(ClanManager.class).getClan(team.getName());
                    if (other != null) {
                        ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, other);
                        team.setPrefix(clanRelation.getPrefix() + other.getTrimmedName() + clanRelation.getSuffix() + " ");
                        team.setSuffix("");
                        if (clanRelation == ClanManager.ClanRelation.ENEMY) {
                            updateDominancePoints(clan, other);
                        }
                    }
                }
            }
        }
    }

    private void updateDominancePoints(Clan clan, Clan enemy) {
        if (getManager(ClanManager.class).getClanRelation(clan, enemy) != ClanManager.ClanRelation.ENEMY) {
            return;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            for (Team team : online.getScoreboard().getTeams()) {
                if (team.getName().equals(noneTeam) || team.getName().contains("@")) {
                    continue;
                }
                team.setSuffix(" " + getEnemyString(clan, enemy));
            }
        }
    }

    public void removeClan(Clan clan) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            for (Team team : online.getScoreboard().getTeams()) {
                if (team.getName().equals(clan.getName())) {
                    team.unregister();
                    for (UUID uuid : clan.getMemberMap().keySet()) {
                        if (Bukkit.getPlayer(uuid) != null) {
                            addNone(Bukkit.getPlayer(uuid));
                        }
                    }
                }
            }
        }
    }

    private void addNone(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getScoreboard().getTeam(noneTeam) == null) {
                online.getScoreboard().registerNewTeam(noneTeam);
                online.getScoreboard().getTeam(noneTeam).setPrefix(ChatColor.YELLOW.toString());
            }
            online.getScoreboard().getTeam(noneTeam).addPlayer(player);
        }
        updateRank(player);
    }

    private void removePlayer(Player player) {
        Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (clan != null) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = online.getScoreboard();
                for (Team team : scoreboard.getTeams()) {
                    if (team.getName().equals(clan.getName())) {
                        team.removePlayer(player);
                        if (!clan.isOnline()) {
                            team.unregister();
                        }
                    }
                }
            }
        }
        addNone(player);
    }

    private String getEnemyString(Clan clan, Clan other) {
        if (clan.getEnemyMap().get(other.getName()) < other.getEnemyMap().get(clan.getName())) {
            return ChatColor.RED + "-" + other.getEnemyMap().get(clan.getName());
        }
        if (clan.getEnemyMap().get(other.getName()) > other.getEnemyMap().get(clan.getName())) {
            return ChatColor.GREEN + "+" + clan.getEnemyMap().get(other.getName());
        }
        return ChatColor.YELLOW + "" + clan.getEnemyMap().get(other.getName());
    }

    private void addClan(Scoreboard scoreboard, Clan clan, Clan other) {
        if (scoreboard.getTeam(clan.getName()) == null) {
            scoreboard.registerNewTeam(clan.getName());
        }
        final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, other);
        scoreboard.getTeam(clan.getName()).setPrefix(clanRelation.getPrefix() + clan.getTrimmedName() + clanRelation.getSuffix() + " ");
    }

    private String getTrimmedRankPrefix(Rank rank) {
        final String str = rank.name() + "@";
        return str.length() > 16 ? str.substring(0, 15) : str;
    }

    @Override
    public ScoreboardPriority getScoreboardPriority() {
        return ScoreboardPriority.LOWEST;
    }
}
