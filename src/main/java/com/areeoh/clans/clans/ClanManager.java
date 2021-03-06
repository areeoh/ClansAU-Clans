package com.areeoh.clans.clans;

import com.areeoh.clans.clans.listeners.*;
import com.areeoh.clans.pillaging.PillageManager;
import com.areeoh.shared.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.Manager;
import com.areeoh.spigot.framework.Module;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.utility.UtilFormat;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ClanManager extends Manager<Module> {

    private HashSet<Clan> clanSet;
    private Map<UUID, ChatType> chatMap;

    public ClanManager(Plugin plugin) {
        super(plugin, "ClansManager");
        this.clanSet = new HashSet<>();
        this.chatMap = new HashMap<>();
    }

    @Override
    public void registerModules() {
        addModule(new ClanListener(this));
        addModule(new ChatListener(this));
        addModule(new ClanMovementListener(this));
        addModule(new ClanGamemodeListener(this));
        addModule(new ClanPlaceBlockListener(this));
        addModule(new ClanBreakBlockListener(this));
        addModule(new ClanInteractListener(this));
        addModule(new ClanDoorListener(this));
        addModule(new ClanDisableLeafDecay(this));
        addModule(new ClanSafeZoneDamageListener(this));
        addModule(new ClanTNTListener(this));
        addModule(new ClanDamageListener(this));
        addModule(new ClanEnergyListener(this));
        addModule(new ClanSkillListener(this));
    }

    public HashSet<Clan> getClanSet() {
        return clanSet;
    }

    public Clan getClan(String world, int x, int z) {
        for(Clan clan : clanSet) {
            for(String claim : clan.getClaims()) {
                if(claim.contains(world + ":" + x + ":" + z)) {
                    return clan;
                }
            }
        }
        return null;
    }

    public Clan getClan(String name) {
        for (Clan clan : clanSet) {
            if (clan.getName().equalsIgnoreCase(name)) {
                return clan;
            }
        }
        return null;
    }

    public Clan getClan(Player player) {
        return getClan(player.getUniqueId());
    }

    public Clan getClan(UUID uuid) {
        for (Clan clan : clanSet) {
            if (clan.getMemberMap().containsKey(uuid)) {
                return clan;
            }
        }
        return null;
    }

    public void addClan(Clan clan) {
        clanSet.add(clan);
        UtilMessage.log("Clans", "Added Clan " + clan.getName() + ".");
    }

    public ClanRelation getClanRelation(Clan clan, Clan other) {
        if (other != null && other.isAdmin()) {
            return ClanRelation.ADMIN;
        }
        if (clan == null || other == null) {
            return ClanRelation.NEUTRAL;
        }
        if (clan.equals(other)) {
            return ClanRelation.SELF;
        }
        if (clan.isTrusted(other)) {
            return ClanRelation.ALLY_TRUSTED;
        }
        if (clan.isAllied(other)) {
            return ClanRelation.ALLY;
        }
        if(getManager(PillageManager.class).isPillaging(clan, other)) {
            return ClanRelation.PILLAGE;
        }
        if(getManager(PillageManager.class).isPillaged(clan, other)) {
            return ClanRelation.PILLAGE_NO_ACCESS;
        }
        if (clan.isEnemy(other)) {
            return ClanRelation.ENEMY;
        }
        return ClanRelation.NEUTRAL;
    }

    public enum ClanRelation {
        SELF(ChatColor.DARK_AQUA, ChatColor.AQUA, (byte) 126),
        NEUTRAL(ChatColor.GOLD, ChatColor.YELLOW, (byte) 74),
        ENEMY(ChatColor.DARK_RED, ChatColor.RED, (byte) 17),
        ALLY(ChatColor.DARK_GREEN, ChatColor.GREEN, (byte) 134),
        ALLY_TRUSTED(ChatColor.GREEN, ChatColor.DARK_GREEN, (byte) 133),
        PILLAGE(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, (byte) 66),
        PILLAGE_NO_ACCESS(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, (byte) 65),
        ADMIN(ChatColor.WHITE, ChatColor.WHITE, (byte) 58);

        private final ChatColor prefix;
        private final ChatColor suffix;
        private final byte mapColor;

        ClanRelation(ChatColor prefix, ChatColor suffix, byte mapColor) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.mapColor = mapColor;
        }

        public byte getMapColor() {
            return mapColor;
        }

        public ChatColor getPrefix() {
            return prefix;
        }

        public ChatColor getSuffix() {
            return suffix;
        }
    }

    public boolean hasAccess(Player p, Location loc) {
        Clan lClan = getClan(loc);
        if (lClan == null) {
            return true;
        }
        final ClanRelation clanRelation = getClanRelation(getClan(p), lClan);
        switch (clanRelation) {
            case PILLAGE:
            case SELF:
            case ALLY_TRUSTED:
                return true;
        }
        return false;
    }

    public boolean canHurt(Player player, Player target, boolean inform) {
        final ClanRelation clanRelation = getClanRelation(getClan(player), getClan(target));
        if (clanRelation == ClanRelation.SELF || clanRelation == ClanRelation.ALLY || clanRelation == ClanRelation.ALLY_TRUSTED) {
            if(inform) {
                UtilMessage.message(player, "Clans", "You cannot harm " + clanRelation.getSuffix() + target.getName() + ChatColor.GRAY + ".");
            }
            return false;
        }
        final Clan playerClan = getClan(player.getLocation());
        if(playerClan != null && playerClan.isSafe()) {
            if(inform) {
                ClanRelation relation = getClanRelation(getClan(player), playerClan);
                UtilMessage.message(player, "Clans", "You cannot harm " + clanRelation.getSuffix() + target.getName() + ChatColor.GRAY + " in " + relation.getSuffix() + playerClan.getName() + ChatColor.GRAY + ".");
            }
            return false;
        }
        final Clan clan = getClan(target.getLocation());
        if(clan != null && clan.isSafe()) {
            if(inform) {
                ClanRelation relation = getClanRelation(getClan(player), clan);
                UtilMessage.message(player, "Clans", "You cannot harm " + clanRelation.getSuffix() + target.getName() + ChatColor.GRAY + " in " + relation.getSuffix() + clan.getName() + ChatColor.GRAY + ".");
            }
            return false;
        }
        return true;
    }

    public final Client searchMember(final Player player, final String name, final boolean inform) {
        final Clan clan = getClan(player.getUniqueId());
        List<Client> members = clan.getMemberMap().keySet().stream().map(uuid -> getManager(ClientManager.class).getClient(uuid)).collect(Collectors.toCollection(ArrayList::new));
        if(members.stream().anyMatch(client -> client.getName().equalsIgnoreCase(name))) {
            return members.stream().filter(client -> clan.getName().equalsIgnoreCase(name)).findFirst().get();
        }
        members.removeIf(client -> !client.getName().toLowerCase().contains(name.toLowerCase()));
        if(members.size() == 1) {
            return members.get(0);
        } else if(members.size() > 1) {
            if(inform) {
                UtilMessage.message(player, "Member Search", ChatColor.YELLOW.toString() + members.size() + ChatColor.GRAY + " matches found [" + members.stream().map(client -> ChatColor.YELLOW + client.getName()).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "]");
            }
        } else {
            UtilMessage.message(player, "Member Search", ChatColor.YELLOW.toString() + members.size() + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + name + ChatColor.GRAY + "]");
        }
        return null;
    }

    public Clan searchClan(Player player, String input, boolean sendMessage) {
        if (clanSet.stream().anyMatch(clan -> clan.getName().equalsIgnoreCase(input))) {
            return clanSet.stream().filter(clan -> clan.getName().equalsIgnoreCase(input)).findFirst().get();
        }
        ArrayList<Clan> clanList = new ArrayList<>(clanSet);
        clanList.removeIf(clan -> !clan.getName().toLowerCase().contains(input.toLowerCase()));
        final Client client = getManager(ClientManager.class).searchClient(player, input, false);
        if (client != null) {
            final Clan clan = getClan(client.getUUID());
            if (clan != null) {
                clanList.add(clan);
            }
        }

        if (clanList.size() == 1) {
            return clanList.get(0);
        } else if(clanList.size() > 1) {
            if (sendMessage) {
                UtilMessage.message(player, "Clan Search", ChatColor.YELLOW.toString() + clanList.size() + ChatColor.GRAY + " matches found [" + clanList.stream().map(clan -> getClanRelation(clan, getClan(player.getUniqueId())).getSuffix() + clan.getName()).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "]");
            }
        } else {
            if (sendMessage) {
                UtilMessage.message(player, "Clan Search", ChatColor.YELLOW.toString() + clanList.size() + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + input + ChatColor.GRAY + "]");
            }
        }
        return null;
    }

    public Clan getClan(Location location) {
        return getClan(location.getChunk());
    }

    public Clan getClan(Chunk chunk) {
        for(Clan clan : clanSet) {
            if(clan.getClaims().contains(UtilFormat.chunkToString(chunk))) {
                return clan;
            }
        }
        return null;
    }

    public String getMemberString(Clan clan) {
        return clan.getMemberMap().entrySet().stream().map(entry -> {
            Client client = getManager(ClientManager.class).getClient(entry.getKey());

            return ChatColor.YELLOW + entry.getValue().name().substring(0, 1) + ChatColor.WHITE + "." + (client instanceof Client ? ChatColor.GREEN : ChatColor.RED) + client.getName();
        }).collect(Collectors.joining(ChatColor.GRAY + ", "));
    }

    public String getEnemyString(Clan clan, Clan other) {
        return other.getEnemyMap().entrySet().stream().map(entry -> {
            final ClanRelation clanRelation = getClanRelation(clan, getClan(entry.getKey()));
            return clanRelation.getSuffix() + entry.getKey() + " " + ChatColor.WHITE + "(" + ChatColor.GREEN + entry.getValue() + ChatColor.WHITE + ":" + ChatColor.RED + getClan(entry.getKey()).getEnemyMap().get(other.getName()) + ChatColor.WHITE + ")";
        }).collect(Collectors.joining(ChatColor.GRAY + ", "));
    }

    public String getAllianceString(Clan clan, Clan other) {
        return other.getAllianceMap().keySet().stream().map(aBoolean -> {
            final ClanRelation clanRelation = getClanRelation(clan, getClan(aBoolean));
            return clanRelation.getSuffix() + aBoolean;
        }).collect(Collectors.joining(ChatColor.GRAY + ", "));
    }

    public String[] getClanTooltip(Clan clan, Clan other) {
        if(clan != null && clan.equals(other)) {
            return new String[] {
                    "Age: " + ChatColor.YELLOW + clan.getAgeString(),
                    "Territory: " + ChatColor.YELLOW + clan.getTerritoryString(),
                    "Home: " + clan.getHomeString(),
                    "Allies: " + getAllianceString(clan, clan),
                    "Enemies: " + getEnemyString(clan, clan),
                    "Members: " + getMemberString(clan),
                    "TNT Protection: " + clan.getTNTProtectedString()
            };
        } else {
            return new String[] {
                    "Age: " + ChatColor.YELLOW + other.getAgeString(),
                    "Territory: " + ChatColor.YELLOW + other.getTerritoryString(),
                    "Allies: " + getAllianceString(clan, other),
                    "Enemies: " + getEnemyString(clan, other),
                    "Members: " + getMemberString(other),
                    "TNT Protection: " + other.getTNTProtectedString()
            };
        }
    }

    public Map<UUID, ChatType> getChatMap() {
        return chatMap;
    }
}
