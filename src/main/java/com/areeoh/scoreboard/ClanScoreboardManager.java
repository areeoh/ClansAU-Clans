package com.areeoh.scoreboard;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.ClansAUCore;
import com.areeoh.client.ClientManager;
import com.areeoh.client.Rank;
import com.areeoh.framework.Manager;
import com.areeoh.framework.Module;
import com.areeoh.scoreboard.ScoreboardManager;
import com.areeoh.scoreboard.listeners.ScoreboardListener;
import com.areeoh.scoreboard.listeners.SideBarHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.UUID;

public class ClanScoreboardManager extends Manager<Module> {

    public ClanScoreboardManager(ClansAUCore plugin) {
        super(plugin, "ClanScoreboardManager");
    }

    @Override
    public void registerModules() {
        addModule(new ScoreboardListener(this));
        addModule(new SideBarHandler(this));
    }

    public void addPlayer(Player player) {
        final Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        for (Map.Entry<UUID, Scoreboard> entry : getManager(ScoreboardManager.class).getScoreboardMap().entrySet()) {
            if (clan == null) {
                if (entry.getValue().getTeam(getManager(ScoreboardManager.class).getNoneTeamName()) == null) {
                    entry.getValue().registerNewTeam(getManager(ScoreboardManager.class).getNoneTeamName());
                }
                entry.getValue().getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).setPrefix(ChatColor.YELLOW + "");
                entry.getValue().getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).addPlayer(player);
            } else {
                addClan(entry.getValue(), clan, getManager(ClanManager.class).getClan(entry.getKey()));
                entry.getValue().getTeam(clan.getName()).addPlayer(player);
            }
        }
        final Scoreboard scoreboard = getManager(ScoreboardManager.class).getScoreboard(player);
        scoreboard.getTeams().forEach(Team::unregister);
        for (Clan c : getManager(ClanManager.class).getClanSet()) {
            if (c.isOnline()) {
                addClan(scoreboard, c, getManager(ClanManager.class).getClan(player.getUniqueId()));
                final Team team = scoreboard.getTeam(c.getName());
                for (UUID member : c.getMemberMap().keySet()) {
                    final Player p = Bukkit.getPlayer(member);
                    if (p != null) {
                        team.addPlayer(p);
                    }
                }
            }
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (getManager(ClanManager.class).getClan(online.getUniqueId()) == null) {
                if (getManager(ClientManager.class).getClient(online.getUniqueId()).hasRank(Rank.MEDIA, false)) {
                    getManager(ScoreboardManager.class).updateRank(online);
                    continue;
                }
                if (scoreboard.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()) == null) {
                    scoreboard.registerNewTeam(getManager(ScoreboardManager.class).getNoneTeamName());
                }
                scoreboard.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).setPrefix(ChatColor.YELLOW + "");
                scoreboard.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).addPlayer(online);
            }
        }
    }

    public void addClan(Scoreboard scoreboard, Clan clan, Clan other) {
        if (scoreboard.getTeam(clan.getName()) == null) {
            scoreboard.registerNewTeam(clan.getName());
        }
        final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, other);
        scoreboard.getTeam(clan.getName()).setPrefix(clanRelation.getPrefix() + clan.getTrimmedName() + clanRelation.getSuffix() + " ");
    }

    public void updateRelation(Clan clan) {
        for (UUID uuid : clan.getMemberMap().keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                for (Team team : player.getScoreboard().getTeams()) {
                    if (team.getName().equals(getManager(ScoreboardManager.class).getNoneTeamName())) {
                        team.setPrefix(ChatColor.YELLOW + "");
                    } else {
                        final Clan other = getManager(ClanManager.class).getClan(team.getName());
                        if (other != null) {
                            final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, other);
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
    }

    public void updateDominancePoints(Clan clan, Clan enemy) {
        if (getManager(ClanManager.class).getClanRelation(clan, enemy) != ClanManager.ClanRelation.ENEMY) {
            return;
        }
        for (Player online : clan.getOnlinePlayers()) {
            for (Team team : online.getScoreboard().getTeams()) {
                if (team.getName().equals(getManager(ScoreboardManager.class).getNoneTeamName()) || team.getName().contains("@")) {
                    continue;
                }
                team.setSuffix(" " + getEnemyString(clan, enemy));
            }
        }
    }

    public void removeClan(Clan clan) {
        for (Map.Entry<UUID, Scoreboard> scoreboard : getManager(ScoreboardManager.class).getScoreboardMap().entrySet()) {
            for (Team team : scoreboard.getValue().getTeams()) {
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

    public void removePlayer(Player player) {
        Clan clan = getManager(ClanManager.class).getClan(player.getUniqueId());
        if (clan != null) {
            for (Scoreboard scoreboard : getManager(ScoreboardManager.class).getScoreboardMap().values()) {
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

    public void addNone(Player player) {
        for (Scoreboard s : getManager(ScoreboardManager.class).getScoreboardMap().values()) {
            if (s.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()) == null) {
                s.registerNewTeam(getManager(ScoreboardManager.class).getNoneTeamName());
                s.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).setPrefix(ChatColor.YELLOW.toString());
            }
            s.getTeam(getManager(ScoreboardManager.class).getNoneTeamName()).addPlayer(player);
        }
        getManager(ScoreboardManager.class).updateRank(player);
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
}
