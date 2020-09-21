package com.areeoh.game.listeners;

import com.areeoh.client.Client;
import com.areeoh.client.ClientManager;
import com.areeoh.framework.updater.Update;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
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
            final Client client = getManager(ClientManager.class).getClient(online.getUniqueId());
            if (client != null) {
                //client.getGamer().addCoins(2000);
                //getManager(ClanScoreboardManager.class).getModule(SideBarHandler.class).updateSidebar(online);
                //getManager(DatabaseManager.class).getModule(ClientRepository.class).saveGamer(client);
                UtilMessage.message(online, "Online Reward", "You received " + ChatColor.YELLOW + "$" + (2000.0F) + ChatColor.GRAY + " Coins.");
            }
        }
    }
}