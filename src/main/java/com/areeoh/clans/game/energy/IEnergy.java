package com.areeoh.clans.game.energy;

import org.bukkit.entity.Player;

public interface IEnergy {
    double getEnergy(Player player);

    void setEnergy(Player player, float energy);

    void regenerateEnergy(Player player, double energy);

    void degenerateEnergy(Player player, double energy);

    boolean use(Player player, String ability, double amount, boolean inform);
}
