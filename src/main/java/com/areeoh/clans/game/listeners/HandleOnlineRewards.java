package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.spigot.client.Client;
import com.areeoh.spigot.client.ClientManager;
import com.areeoh.spigot.framework.updater.Update;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class HandleOnlineRewards extends GameModule implements Listener {

    public HandleOnlineRewards(GameManager manager) {
        super(manager, "HandleOnlineRewards");
    }

    @Update(ticks = 72000)
    public void HourlyReward() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            final Client client = getManager(ClientManager.class).getOnlineClient(online.getUniqueId());
            if (client != null) {
                //client.getGamer().addCoins(2000);
                //getManager(ClanScoreboardManager.class).getModule(SideBarHandler.class).updateSidebar(online);
                //getManager(DatabaseManager.class).getModule(ClientRepository.class).saveGamer(client);
                UtilMessage.message(online, "Online Reward", "You received " + ChatColor.YELLOW + "$" + (2000.0F) + ChatColor.GRAY + " Coins.");
            }
        }
    }
}