package com.areeoh.game.energy;

import com.areeoh.client.ClientManager;
import com.areeoh.framework.updater.Update;
import com.areeoh.framework.updater.Updater;
import com.areeoh.utility.UtilMessage;
import com.areeoh.game.GameManager;
import com.areeoh.game.GameModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Energy extends GameModule implements IEnergy, Listener, Updater {

    public Energy(GameManager manager) {
        super(manager, "Energy");
    }

    @Override
    public double getEnergy(Player player) {
        return player.getExp();
    }

    @Override
    public void setEnergy(Player player, float energy) {
        player.setExp(energy);
    }

    @Override
    public void regenerateEnergy(Player player, double energy) {
        player.setExp((float) Math.min(0.999F, getEnergy(player) + energy));
    }

    @Override
    public void degenerateEnergy(Player player, double energy) {
        player.setExp((float) Math.min(0.999F, getEnergy(player) - energy));
    }

    @Override
    public boolean use(Player player, String ability, double amount, boolean inform) {
        if (getManager(ClientManager.class).getClient(player.getUniqueId()).isAdministrating()) {
            return true;
        }
        amount = 0.999D * (amount / 100.0D);
        if (amount > getEnergy(player)) {
            if (inform) {
                UtilMessage.message(player, "Energy", "You are too exhausted to use " + ChatColor.GREEN + ability + ChatColor.GRAY + ".");
            }
            return false;
        }
        if (!getManager(ClientManager.class).getClient(player.getUniqueId()).isAdministrating()) {
            degenerateEnergy(player, amount);
        }
        return true;
    }

    private void updateEnergy(Player player) {
        if (player.isDead()) {
            return;
        }
        if (player.isSprinting()) {
            double energy = 16.0E-4D;
            regenerateEnergy(player, energy);
            return;
        }
        double energy = 0.006D;
        regenerateEnergy(player, energy);
    }

    @Update(ticks = 1) //TODO test this
    public void Update() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            updateEnergy(online);
        }
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        setEnergy(event.getPlayer(), 150.0F);
    }

    @EventHandler
    public void handleExp(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            use(player, "Attack", 0.02D, false);
        }

    }
}
