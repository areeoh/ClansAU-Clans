package com.areeoh.game.listeners;

import com.areeoh.client.Client;
import com.areeoh.client.ClientManager;
import com.areeoh.utility.UtilMath;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ReceiveCoinsForKill extends GameModule implements Listener {

    public ReceiveCoinsForKill(GameManager manager) {
        super(manager, "ReceiveCoinsForKill");
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if(event.getEntity() == null) {
            return;
        }
        if(event.getEntity() instanceof Player) {
            return;
        }
        if(event.getEntity().getKiller() == null) {
            return;
        }
        final Player killer = event.getEntity().getKiller();
        final Client client = getManager(ClientManager.class).getClient(killer.getUniqueId());
        final int amount = UtilMath.randomInt((int) (event.getEntity().getMaxHealth() * 14), (int) (event.getEntity().getMaxHealth() * 20));
        //client.getGamer().addCoins(amount);
        //getManager(ClanScoreboardManager.class).getModule(SideBarHandler.class).updateSideBar(killer);
        //getManager(DatabaseManager.class).getModule(ClientRepository.class).saveGamer(client);
        UtilMessage.message(event.getEntity().getKiller(), "Kill Reward", "You received " + ChatColor.YELLOW + "$" + amount + ChatColor.GRAY + " for killing " + ChatColor.GREEN + event.getEntity().getName() + ChatColor.GRAY + ".");
    }
}