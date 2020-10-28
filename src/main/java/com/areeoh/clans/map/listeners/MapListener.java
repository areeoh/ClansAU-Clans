package com.areeoh.clans.map.listeners;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.events.*;
import com.areeoh.clans.map.MapManager;
import com.areeoh.clans.map.data.ChunkData;
import com.areeoh.clans.map.data.MapSettings;
import com.areeoh.core.framework.Module;
import com.areeoh.core.recharge.RechargeManager;
import com.areeoh.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MapListener extends Module<MapManager> implements Listener {

    public MapListener(MapManager manager) {
        super(manager, "MapListener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        getManager().clanMapData.remove(player.getUniqueId());
        getManager().mapSettingsMap.remove(player.getUniqueId());
        for (ItemStack value : player.getInventory().all(Material.MAP).values()) {
            player.getInventory().remove(value);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadChunks(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(itemStack -> itemStack.getType() == Material.MAP);
    }

    @EventHandler
    public void onMapTransfer(InventoryClickEvent event) {
        if (event.getCurrentItem() == null)
            return;

        if (event.getCurrentItem().getType() == Material.MAP) {
            final Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
            if(topInventory != null && topInventory.getType() != InventoryType.CRAFTING) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEvent(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && item.getType() == Material.MAP) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEvent(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item != null && item.getType() == Material.MAP) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanAlly(ClanAllyEvent event) {
        updateClanRelation(event.getClan(), event.getOther());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanEnemy(ClanEnemyEvent event) {
        updateClanRelation(event.getClan(), event.getOther());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanTrust(ClanTrustEvent event) {
        updateClanRelation(event.getClan(), event.getOther());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanNeutral(ClanNeutralEvent event) {
        updateClanRelation(event.getClan(), event.getOther());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanRevokeTrust(ClanRevokeTrustEvent event) {
        updateClanRelation(event.getClan(), event.getOther());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPillageStart(ClanPillageStartEvent event) { updateClanRelation(event.getPillager(), event.getPillagee()); }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPillageEnd(ClanPillageEndEvent event) { updateClanRelation(event.getPillager(), event.getPillagee()); }

    //TODO ADD PILLAGE EVENT
    private void updateClanRelation(Clan clan, Clan other) {
        ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, other);
        byte color;
        color = clanRelation.getMapColor();
        if (clan.getName().equals("Fields")) {
            color = 62;
        }
        if (clan.getName().equals("Red Shops") || clan.getName().equals("Red Spawn")) {
            color = 114;
        }
        if (clan.getName().equals("Blue Shops") || clan.getName().equals("Blue Spawn")) {
            color = (byte) 129;
        }
        if (clan.getName().equals("Outskirts")) {
            color = 74;
        }
        for (UUID uuid : other.getMemberMap().keySet()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member == null) {
                continue;
            }
            if (!getManager().clanMapData.containsKey(uuid)) {
                getManager().clanMapData.put(uuid, new HashSet<>());
            }
            final Set<ChunkData> clanMapData = getManager().clanMapData.get(uuid);
            clanMapData.stream().filter(chunkData -> chunkData.getClan().equals(clan.getName())).forEach(chunkData -> chunkData.setColor(clanRelation.getMapColor()));
            if (clanMapData.stream().noneMatch(chunkData -> chunkData.getClan().equals(clan.getName()))) {
                for (String claim : clan.getClaims()) {
                    final String[] split = claim.split(":");
                    clanMapData.add(new ChunkData(split[0], color, Integer.parseInt(split[1]), Integer.parseInt(split[2]), clan.getName()));
                }
            }
            updateStatus(member);
        }
        for (UUID uuid : clan.getMemberMap().keySet()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member == null) {
                continue;
            }
            if (!getManager().clanMapData.containsKey(uuid)) {
                getManager().clanMapData.put(uuid, new HashSet<>());
            }

            final Set<ChunkData> clanMapData = getManager().clanMapData.get(uuid);
            clanMapData.stream().filter(chunkData -> chunkData.getClan().equals(other.getName())).forEach(chunkData -> chunkData.setColor(clanRelation.getMapColor()));
            if (clanMapData.stream().noneMatch(chunkData -> chunkData.getClan().equals(other.getName()))) {
                for (String claim : other.getClaims()) {
                    final String[] split = claim.split(":");
                    clanMapData.add(new ChunkData(split[0], color, Integer.parseInt(split[1]), Integer.parseInt(split[2]), other.getName()));
                }
            }
            updateStatus(member);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUnclaim(ClanUnclaimEvent event) {
        updateClaims(event.getClan());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaim(ClanClaimEvent event) {
        updateClaims(event.getClan());
    }

    private void updateClaims(Clan clan) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            final ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, getManager(ClanManager.class).getClan(online));
            byte color;
            color = clanRelation.getMapColor();
            if (clan.getName().equals("Fields")) {
                color = 62;
            }
            if (clan.getName().equals("Red Shops") || clan.getName().equals("Red Spawn")) {
                color = 114;
            }
            if (clan.getName().equals("Blue Shops") || clan.getName().equals("Blue Spawn")) {
                color = (byte) 129;
            }
            if (clan.getName().equals("Outskirts")) {
                color = 74;
            }
            if (!getManager().clanMapData.containsKey(online.getUniqueId())) {
                getManager().clanMapData.put(online.getUniqueId(), new HashSet<>());
            }
            getManager().clanMapData.get(online.getUniqueId()).removeIf(chunkData -> getManager(ClanManager.class).getClan(online.getWorld().getName(), chunkData.getX(), chunkData.getZ()) == null);
            for (String claim : clan.getClaims()) {
                final String[] split = claim.split(":");
                final String world = split[0];
                final int x = Integer.parseInt(split[1]);
                final int z = Integer.parseInt(split[2]);
                if (getManager().clanMapData.get(online.getUniqueId()).stream().noneMatch(chunkData -> chunkData.getX() == x && chunkData.getZ() == z && chunkData.getClan().equals(clan.getName()))) {
                    final ChunkData e = new ChunkData(world, color, x, z, clan.getName());
                    getManager().clanMapData.get(online.getUniqueId()).add(e);
                }
            }
            for (ChunkData chunkData : getManager().clanMapData.get(online.getUniqueId())) {
                chunkData.getBlockFaceSet().clear();
                for (int i = 0; i < 4; i++) {
                    BlockFace blockFace = BlockFace.values()[i];
                    final Clan other = getManager(MapManager.class).getManager(ClanManager.class).getClan(online.getWorld().getName(), chunkData.getX() + blockFace.getModX(), chunkData.getZ() + blockFace.getModZ());
                    if (other != null && chunkData.getClan().equals(other.getName())) {
                        chunkData.getBlockFaceSet().add(blockFace);
                    }
                }
            }
            updateStatus(online);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanLeave(ClanLeaveEvent event) {
        final Player player = event.getPlayer();
        if (!getManager().clanMapData.containsKey(player.getUniqueId())) {
            getManager().clanMapData.put(player.getUniqueId(), new HashSet<>());
        }
        for (ChunkData chunkData : getManager().clanMapData.get(player.getUniqueId())) {
            final Clan clan = getManager(ClanManager.class).getClan(chunkData.getClan());
            if (clan != null && !clan.isAdmin()) {
                chunkData.setColor(ClanManager.ClanRelation.NEUTRAL.getMapColor());
            }
        }
        updateStatus(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisband(ClanDisbandEvent event) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!getManager().clanMapData.containsKey(online.getUniqueId())) {
                getManager().clanMapData.put(online.getUniqueId(), new HashSet<>());
            }
            getManager().clanMapData.get(online.getUniqueId()).removeIf(chunkData -> getManager(ClanManager.class).getClan(chunkData.getClan()) == null);
            for (ChunkData chunkData : getManager().clanMapData.get(online.getUniqueId())) {
                final Clan clan = getManager(ClanManager.class).getClan(chunkData.getClan());
                if (clan != null && !clan.isAdmin()) {
                    chunkData.setColor(getManager(ClanManager.class).getClanRelation(clan, getManager(ClanManager.class).getClan(online.getUniqueId())).getMapColor());
                }
            }
            updateStatus(online);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinClan(ClanJoinEvent event) {
        final Player player = event.getPlayer();
        final Clan clan = getManager(ClanManager.class).getClan(player);
        if (clan == null) {
            return;
        }
        if (!getManager().clanMapData.containsKey(player.getUniqueId())) {
            getManager().clanMapData.put(player.getUniqueId(), new HashSet<>());
        }
        for (String claim : clan.getClaims()) {
            final int x = Integer.parseInt(claim.split(":")[1]);
            final int z = Integer.parseInt(claim.split(":")[2]);
            getManager().clanMapData.get(player.getUniqueId()).stream().filter(chunkData -> chunkData.getX() == x && chunkData.getZ() == z && chunkData.getClan().equals(clan.getName())).forEach(chunkData -> chunkData.setColor(ClanManager.ClanRelation.SELF.getMapColor()));
        }
        for (String ally : clan.getAllianceMap().keySet()) {
            Clan allyClan = getManager(ClanManager.class).getClan(ally);
            ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, allyClan);
            for (String claim : allyClan.getClaims()) {
                final int x = Integer.parseInt(claim.split(":")[1]);
                final int z = Integer.parseInt(claim.split(":")[2]);
                getManager().clanMapData.get(player.getUniqueId()).stream().filter(chunkData -> chunkData.getX() == x && chunkData.getZ() == z && chunkData.getClan().equals(ally)).forEach(chunkData -> chunkData.setColor(clanRelation.getMapColor()));
            }
        }
        for (String enemy : clan.getAllianceMap().keySet()) {
            Clan enemyClan = getManager(ClanManager.class).getClan(enemy);
            ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(clan, enemyClan);
            for (String claim : enemyClan.getClaims()) {
                final int x = Integer.parseInt(claim.split(":")[1]);
                final int z = Integer.parseInt(claim.split(":")[2]);
                getManager().clanMapData.get(player.getUniqueId()).stream().filter(chunkData -> chunkData.getX() == x && chunkData.getZ() == z && chunkData.getClan().equals(enemy)).forEach(chunkData -> chunkData.setColor(clanRelation.getMapColor()));
            }
        }
        updateStatus(player);
    }

    private void loadChunks(Player player) {
        if (!getManager().clanMapData.containsKey(player.getUniqueId())) {
            getManager().clanMapData.put(player.getUniqueId(), new HashSet<>());
        }

        final Set<ChunkData> chunkClaimColor = getManager().clanMapData.get(player.getUniqueId());

        final Clan pClan = getManager(ClanManager.class).getClan(player);
        for (Clan clan : getManager(ClanManager.class).getClanSet()) {
            ClanManager.ClanRelation clanRelation = getManager(ClanManager.class).getClanRelation(pClan, clan);
            for (String claim : clan.getClaims()) {
                byte color;
                color = clanRelation.getMapColor();
                if (clan.getName().equals("Fields")) {
                    color = 62;
                }
                if (clan.getName().equals("Red Shops") || clan.getName().equals("Red Spawn")) {
                    color = 114;
                }
                if (clan.getName().equals("Blue Shops") || clan.getName().equals("Blue Spawn")) {
                    color = (byte) 129;
                }
                if (clan.getName().equals("Outskirts")) {
                    color = 74;
                }
                final String[] split = claim.split(":");
                final ChunkData e = new ChunkData(split[0], color, Integer.parseInt(split[1]), Integer.parseInt(split[2]), clan.getName());

                for (int i = 0; i < 4; i++) {
                    BlockFace blockFace = BlockFace.values()[i];
                    final Clan other = getManager(MapManager.class).getManager(ClanManager.class).getClan(player.getWorld().getName(), e.getX() + blockFace.getModX(), e.getZ() + blockFace.getModZ());
                    if (other != null && e.getClan().equals(other.getName())) {
                        e.getBlockFaceSet().add(blockFace);
                    }
                }
                chunkClaimColor.add(e);
            }
        }
        updateStatus(player);
    }

    private void updateStatus(Player player) {
        if(!getManager().mapSettingsMap.containsKey(player.getUniqueId())) {
            getManager().mapSettingsMap.put(player.getUniqueId(), new MapSettings(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
        }
        getManager().mapSettingsMap.get(player.getUniqueId()).setUpdate(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if(player.getItemInHand() == null || player.getItemInHand().getType() != Material.MAP) {
            return;
        }
        if(!(event.getAction().name().contains("RIGHT") || event.getAction().name().contains("LEFT"))) {
            return;
        }
        if (!getManager().mapSettingsMap.containsKey(player.getUniqueId())) {
            getManager().mapSettingsMap.put(player.getUniqueId(), new MapSettings(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
        }
        final MapSettings mapSettings = getManager().mapSettingsMap.get(player.getUniqueId());

        if(!getManager(RechargeManager.class).use(player, "Map Zoom", 250, false, false)) {
            return;
        }

        if (event.getAction().name().contains("RIGHT")) {
            MapSettings.Scale curScale = mapSettings.getScale();

            if (curScale == MapSettings.Scale.FAR) {
                return;
            }
            UtilPlayer.sendActionBar(player, createZoomBar(mapSettings.setScale(MapSettings.Scale.values()[curScale.ordinal() + 1])));
            mapSettings.setUpdate(true);
        } else if (event.getAction().name().contains("LEFT")) {
            MapSettings.Scale curScale = mapSettings.getScale();

            if (curScale == MapSettings.Scale.CLOSEST) {
                return;
            }
            UtilPlayer.sendActionBar(player, createZoomBar(mapSettings.setScale(MapSettings.Scale.values()[curScale.ordinal() - 1])));
            mapSettings.setUpdate(true);
        }
    }

    private String createZoomBar(MapSettings.Scale scale) {
        return ChatColor.WHITE + "Zoom Factor: " + ChatColor.GREEN + (1 << scale.getValue()) + "x";
    }
}