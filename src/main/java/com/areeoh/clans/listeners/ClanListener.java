package com.areeoh.clans.listeners;

import com.areeoh.blockregen.BlockRegenManager;
import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.ClanRepository;
import com.areeoh.clans.events.*;
import com.areeoh.client.Client;
import com.areeoh.database.DatabaseManager;
import com.areeoh.framework.Module;
import com.areeoh.utility.UtilFormat;
import com.areeoh.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ClanListener extends Module<ClanManager> implements Listener {

    public ClanListener(ClanManager manager) {
        super(manager, "ClanListener");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanAlly(ClanAllyEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = event.getClan();
        final Clan other = event.getOther();
        other.getAllianceRequestMap().remove(clan.getName());
        clan.getAllianceRequestMap().remove(other.getName());
        clan.getAllianceMap().put(other.getName(), false);
        other.getAllianceMap().put(clan.getName(), false);
        UtilMessage.message(event.getPlayer(), "Clans", "You have accepted alliance with " + getManager(ClanManager.class).getClanRelation(clan, other).getSuffix() + "Clan " + other.getName() + ChatColor.GRAY + ".");
        other.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, other).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has accepted alliance with you.", null);
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has accepted alliance with your Clan.", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(clan);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(other);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanClaim(ClanClaimEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Chunk chunk = event.getChunk();
        final Player player = event.getPlayer();
        getManager(BlockRegenManager.class).outlineChunk(chunk);
        //TODO UPDATE SCOREBOARD
        clan.getClaims().add(UtilFormat.chunkToString(chunk));
        UtilMessage.message(player, "Clans", "You claimed land " + ChatColor.YELLOW + "(" + chunk.getX() + "," + chunk.getZ() + ")" + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " claimed land " + ChatColor.YELLOW + "(" + chunk.getX() + "," + chunk.getZ() + ")" + ChatColor.GRAY + ".", player.getUniqueId());
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateClaims(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanCreate(ClanCreateEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = event.getClan();
        clan.getMemberMap().put(player.getUniqueId(), Clan.MemberRole.LEADER);
        getManager(ClanManager.class).getClanSet().add(clan);
        UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " formed " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        getManager(DatabaseManager.class).getModule(ClanRepository.class).saveClan(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanDemote(ClanDemoteEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Client target = event.getTarget();
        final Clan clan = event.getClan();
        clan.getMemberMap().put(target.getUUID(), Clan.MemberRole.values()[clan.getMemberRole(target.getUUID()).ordinal() - 1]);
        UtilMessage.message(player, "Clans", "You demoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + UtilFormat.cleanString(clan.getMemberRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " demoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getMemberRole(target.getUUID()).name()) + ChatColor.GRAY + ".", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateMembers(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanDisband(ClanDisbandEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        if(player != null) {
            UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has disbanded " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        }
        getManager(DatabaseManager.class).getModule(ClanRepository.class).deleteClan(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanEnemy(ClanEnemyEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Clan target = event.getOther();
        final Player player = event.getPlayer();

        if(clan.isAllied(target)) {
            clan.getAllianceMap().remove(target.getName());
            target.getAllianceMap().remove(clan.getName());
            getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(clan);
            getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(target);
        }
        clan.getAllianceRequestMap().remove(target.getName());
        target.getAllianceRequestMap().remove(clan.getName());
        clan.getEnemyMap().put(target.getName(), 0);
        target.getEnemyMap().put(clan.getName(), 0);
        UtilMessage.message(player, "Clans", "You waged war with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        target.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " waged war with your Clan.", null);
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " waged war with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(clan);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(target);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanHome(ClanHomeEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = event.getClan();

        player.teleport(clan.getHome().getBlock().getLocation());
        UtilMessage.message(player, "Clans", "You teleported to clan home " + ChatColor.YELLOW + "(" + (int) clan.getHome().getX() + "," + (int) clan.getHome().getZ() + ")" + ChatColor.GRAY + ".");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanInvite(ClanInviteEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Player target = event.getTarget();

        clan.getInviteeMap().put(target.getUniqueId(), System.currentTimeMillis());
        clan.inform( true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " invited " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to your Clan.", player.getUniqueId());
        UtilMessage.message(player, "Clans", "You invited " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        UtilMessage.message(target, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has invited you to join " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        //TODO ADD FANCY MESSAGE HERE CLICKABLE TEXT
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanJoin(ClanJoinEvent event) {
        if(event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final Clan target = event.getClan();

        target.getInviteeMap().remove(player.getUniqueId());
        target.getMemberMap().put(player.getUniqueId(), Clan.MemberRole.RECRUIT);
        UtilMessage.message(player, "Clans", "You joined " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
        target.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " joined your Clan.", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateMembers(target);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanKick(ClanKickEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Client target = event.getTarget();

        clan.getMemberMap().remove(target.getUUID());
        UtilMessage.message(player, "Clans", "You kicked " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " from your clan.");
        if (Bukkit.getPlayer(target.getUUID()) != null) {
            UtilMessage.message(Bukkit.getPlayer(target.getUUID()), "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " kicked you from " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        }
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " kicked " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " from your Clan.", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateMembers(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanLeave(ClanLeaveEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Clan clan = event.getClan();

        clan.getMemberMap().remove(player.getUniqueId());
        UtilMessage.message(player, "Clans", "You left " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " left your Clan.", player.getUniqueId());
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateMembers(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNeutralClan(ClanNeutralEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Clan target = event.getOther();
        final Player player = event.getPlayer();
        if(clan.isAllied(target)) {
            clan.getAllianceMap().remove(target.getName());
            target.getAllianceMap().remove(clan.getName());
            UtilMessage.message(player, "Clans", "You are now neutral with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.inform(true, "Clans", "Your clan is now neutral with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());
            target.inform(true, "Clans", "Your clan is now neutral with " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".", null);

            getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(clan);
            getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(target);
            return;
        }
        UtilMessage.message(player, "Clans", "You have accepted neutrality with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
        target.inform(true, "Clans", ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has accepted neutrality with you.", null);
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " accepted neutrality with your Clan.", player.getUniqueId());
        target.getAllianceRequestMap().remove(clan.getName());
        clan.getAllianceRequestMap().remove(target.getName());
        target.getNeutralRequestMap().remove(clan.getName());
        clan.getNeutralRequestMap().remove(target.getName());
        clan.getEnemyMap().remove(target.getName());
        target.getEnemyMap().remove(clan.getName());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(clan);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(target);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanPromote(ClanPromoteEvent event) {
        final Client target = event.getTarget();
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        if(clan.getMemberRole(target.getUUID()) == Clan.MemberRole.ADMIN) {
            clan.getMemberMap().put(player.getUniqueId(), Clan.MemberRole.ADMIN);
        }
        clan.getMemberMap().put(target.getUUID(), Clan.MemberRole.values()[clan.getMemberRole(target.getUUID()).ordinal() + 1]);
        UtilMessage.message(player, "Clans", "You promoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + UtilFormat.cleanString(clan.getMemberRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " promoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getMemberRole(target.getUUID()).name()) + ChatColor.GRAY + ".", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateMembers(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanSetHome(ClanSetHomeEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        clan.setHome(player.getLocation().getBlock().getLocation());
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " set Clan Home at " + ChatColor.YELLOW + "(" + (int) player.getLocation().getX() + "," + (int) player.getLocation().getZ() + ")" + ChatColor.GRAY + ".", player.getUniqueId());
        UtilMessage.message(player, "Clans", "You set the Clan Home at " + ChatColor.YELLOW + "(" + (int) player.getLocation().getX() + "," + (int) player.getLocation().getZ() + ")" + ChatColor.GRAY + ".");
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateHome(clan);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanTrust(ClanTrustEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Clan target = event.getOther();
        final Player player = event.getPlayer();

        target.getTrustRequestMap().remove(clan.getName());
        clan.getTrustRequestMap().remove(target.getName());
        clan.getAllianceMap().put(target.getName(), true);
        target.getAllianceMap().put(clan.getName(), true);

        UtilMessage.message(player, "Clans", "You have accepted trust with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        target.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has accepted trust with you.", null);
        clan.inform( true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " accepted trust with your Clan.", player.getUniqueId());

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(clan);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(target);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanRevokeTrust(ClanRevokeTrustEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Clan target = event.getOther();
        final Player player = event.getPlayer();

        clan.getAllianceMap().put(target.getName(), false);
        target.getAllianceMap().put(clan.getName(), false);
        UtilMessage.message(player, "Clans", "You have revoked trust with " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has revoked trust to " + getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId());
        target.inform(true, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + "Clan " + clan.getName() + ChatColor.GRAY + " has revoked trust to you.", null);

        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(clan);
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(target);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClanUnclaim(ClanUnclaimEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        if (clan.getHome() != null) {
            if (clan.getHome().getChunk().equals(player.getLocation().getChunk())) {
                clan.setHome(null);
                getManager(DatabaseManager.class).getModule(ClanRepository.class).updateHome(clan);
            }
        }
        clan.getClaims().remove(UtilFormat.chunkToString(player.getLocation().getChunk()));
        UtilMessage.message(player, "Clans", "You unclaimed land " + ChatColor.YELLOW + "(" + player.getLocation().getChunk().getX() + "," + ChatColor.YELLOW + player.getLocation().getChunk().getZ() + ChatColor.YELLOW + ")" + ChatColor.GRAY + ".");
        clan.inform(true, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " unclaimed land " + ChatColor.YELLOW + "(" + player.getLocation().getChunk().getX() + "," + player.getLocation().getChunk().getZ() + ")" + ChatColor.GRAY + ".", player.getUniqueId());
        getManager(DatabaseManager.class).getModule(ClanRepository.class).updateClaims(clan);
    }
}
