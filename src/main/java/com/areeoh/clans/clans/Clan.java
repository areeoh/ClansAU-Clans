package com.areeoh.clans.clans;

import com.areeoh.spigot.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Clan {

    private String name;
    private long age;
    private Location home;
    private int energy;
    private boolean admin;
    private boolean safe;
    private long lastLogin;
    private HashSet<String> claims;
    private HashMap<UUID, Long> inviteeMap;
    private HashMap<String, Long> allianceRequestMap;
    private HashMap<String, Long> trustRequestMap;
    private HashMap<String, Long> neutralRequestMap;
    private HashMap<String, Boolean> allianceMap;
    private HashMap<String, Integer> enemyMap;
    private HashMap<UUID, MemberRole> memberMap;

    public Clan(String name) {
        this.name = name;
        this.age = System.currentTimeMillis();
        this.home = null;
        this.energy = 2400;
        this.admin = false;
        this.safe = false;
        this.lastLogin = System.currentTimeMillis();
        this.claims = new HashSet<>();
        this.inviteeMap = new HashMap<>();
        this.allianceRequestMap = new HashMap<>();
        this.trustRequestMap = new HashMap<>();
        this.neutralRequestMap = new HashMap<>();
        this.allianceMap = new HashMap<>();
        this.enemyMap = new HashMap<>();
        this.memberMap = new HashMap<>();
    }

    public String getTrimmedName() {
        return getName().length() > 8 ? getName().substring(0, 8) : getName();
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public HashSet<String> getClaims() {
        return claims;
    }

    public void inform(boolean enablePrefix, String prefix, String message, UUID... uuid) {
        memberMap.keySet().stream().filter(member -> !Arrays.asList(uuid).contains(member)).filter(member -> Bukkit.getPlayer(member) != null).forEach(member -> Bukkit.getPlayer(member).sendMessage((enablePrefix ? ChatColor.BLUE + prefix + "> " + ChatColor.GRAY : "") + message));
    }

    public MemberRole getMemberRole(UUID uuid) {
        return memberMap.get(uuid);
    }

    public boolean isEnemy(Clan other) {
        if(other == null) {
            return false;
        }
        return getEnemyMap().containsKey(other.getName());
    }

    public boolean isAllied(Clan other) {
        if(other == null) {
            return false;
        }
        return getAllianceMap().containsKey(other.getName());
    }

    public boolean isTrusted(Clan other) {
        if(other == null) {
            return false;
        }
        if(!getAllianceMap().containsKey(other.getName())) {
            return false;
        }
        return getAllianceMap().get(other.getName());
    }

    public double getEnergyFromHours(int hour) {
        return hour * (24.0D * (getClaims().isEmpty() ? 12.5F : getClaims().size() * 25.0D)) / 24;
    }

    public double getHoursOfEnergy() {
        return getEnergy() / (getClaims().isEmpty() ? 12.5D : getClaims().size() * 25.0D);
    }

    public String getEnergyString() {
        double days = UtilTime.trim(getHoursOfEnergy() / 24, 2);
        if(days < 1.0D) {
            return UtilTime.trim(days * 24, 2) + " Hours";
        } else if (days == 1.0D) {
            return days + " Day";
        } else {
            return days + " Days";
        }
    }

    public String getAgeString() {
        return UtilTime.getTime(System.currentTimeMillis() - getAge(), UtilTime.TimeUnit.BEST, 1);
    }

    public String getTerritoryString() {
        return getClaims().size() + "/" + getMaxClaims();
    }

    public String getHomeString() {
        if(getHome() == null) {
            return ChatColor.RED + "Not set";
        }
        return ChatColor.YELLOW + "(" + (int)getHome().getX() + "," + (int)getHome().getZ() + ")";
    }

    public boolean isTNTProtected() {
        if (getMemberMap().isEmpty()) {
            return true;
        }
        if (getMemberMap().keySet().stream().anyMatch(uuid -> Bukkit.getPlayer(uuid) != null)) {
            return getMemberMap().keySet().stream().anyMatch(uuid -> Bukkit.getPlayer(uuid) != null);
        }
        if (isAdmin()) {
            return true;
        }
        return !UtilTime.elapsed(lastLogin, 900000L);
    }

    public String getTNTProtectedString() {
        return (isOnline() ? ChatColor.GOLD + "No, Clan Members are online." : !isTNTProtected() ? ChatColor.GREEN + "Yes, TNT Protected." : ChatColor.GOLD + "No, " + UtilTime.getTime(getLastLogin() + 900000L - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1) + " until protection.");
    }

    public boolean isOnline() {
        return getMemberMap().keySet().stream().anyMatch(uuid -> Bukkit.getPlayer(uuid) != null);
    }

    public boolean hasRole(UUID uuid, MemberRole memberRole) {
        return getMemberRole(uuid).ordinal() >= memberRole.ordinal();
    }

    public String getDominanceString(Clan other) {
        return ChatColor.WHITE + "(" + ChatColor.GREEN + getEnemyMap().get(other.getName()) + ChatColor.WHITE + ":" + ChatColor.RED + other.getEnemyMap().get(getName()) + ChatColor.WHITE + ")" + ChatColor.RESET;
    }

    public int getMaxClaims() {
        return getMemberMap().size() + 3;
    }

    public HashMap<UUID, Long> getInviteeMap() {
        return inviteeMap;
    }

    public HashMap<String, Long> getAllianceRequestMap() {
        return allianceRequestMap;
    }

    public HashMap<String, Long> getNeutralRequestMap() {
        return neutralRequestMap;
    }

    public HashMap<String, Boolean> getAllianceMap() {
        return allianceMap;
    }

    public HashMap<String, Integer> getEnemyMap() {
        return enemyMap;
    }

    public long getAge() {
        return age;
    }

    public Location getHome() {
        return home;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isSafe() {
        return safe;
    }

    public boolean isSafe(Location loc) {
        if(!isAdmin()) {
            return false;
        }
        if (!getName().toLowerCase().contains("spawn")) {
            return isSafe();
        }
        return loc.getY() >= 80;
    }

    public void playSound(Sound sound, float volume, float pitch) {
        for(Player player : getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, pitch, volume);
        }
    }

    public Set<Player> getOnlinePlayers() {
        return getMemberMap().keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(uuid -> Bukkit.getPlayer(uuid)).collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<UUID, MemberRole> getMemberMap() {
        return memberMap;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public HashMap<String, Long> getTrustRequestMap() {
        return trustRequestMap;
    }

    public enum MemberRole {
        RECRUIT,
        MEMBER,
        ADMIN,
        LEADER
    }
}