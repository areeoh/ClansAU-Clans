package com.areeoh.clans.clans.listeners;

import com.areeoh.clans.clans.ChatType;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.shared.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.client.Rank;
import com.areeoh.spigot.framework.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends Module<ClanManager> implements Listener {

    public ChatListener(ClanManager manager) {
        super(manager, "ChatListener");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        Clan clan = getManager().getClan(player);
        String message = event.getMessage();
        if(getManager().getChatMap().getOrDefault(player.getUniqueId(), ChatType.NONE) == ChatType.CLAN) {
            clan.inform(false, null, ChatColor.AQUA + player.getName() + " " + ChatColor.DARK_AQUA + message);
            return;
        }
        if(getManager().getChatMap().getOrDefault(player.getUniqueId(), ChatType.NONE) == ChatType.ALLY) {
            clan.inform(false, null, ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + message);
            for (String s : clan.getAllianceMap().keySet()) {
                getManager().getClan(s).inform(false, null, ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + message);
            }
            return;
        }
        ClientManager manager = getManager(ClientManager.class);
        Client client = manager.getClient(player.getUniqueId());
        for (Player online : Bukkit.getOnlinePlayers()) {
            if(manager.getClient(online.getUniqueId()).getIgnore().contains(player.getUniqueId())) {
                continue;
            }
            Clan target = getManager().getClan(online);
            String rank = "";
            if(client.hasRank(Rank.HELPER, false)) {
                rank = client.getRank().getRankColor(true) + " ";
            }
            if(clan == null) {
                online.sendMessage(rank + ChatColor.YELLOW + player.getName() + ChatColor.RESET + ": " + message);
                continue;
            }
            online.sendMessage(rank + getManager().getClanRelation(clan, target).getPrefix() + clan.getName() + " " + getManager().getClanRelation(clan, target).getSuffix() + player.getName() + ChatColor.RESET + ": " + message);
        }
    }
}