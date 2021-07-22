package com.areeoh.clans.shops.commands;

import com.areeoh.clans.shops.ShopRepository;
import com.areeoh.spigot.client.Rank;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.repository.RepositoryManager;
import org.bukkit.entity.Player;

public class ShopSpawnCommand extends Command<Player> {

    public ShopSpawnCommand(CommandManager manager) {
        super(manager, "ShopSpawnCommand", Player.class);
        setCommand("create");
        setIndex(1);
        setRequiredRank(Rank.OWNER);
    }

    @Override
    public boolean execute(Player player, String[] strings) {
        getManager(RepositoryManager.class).getModule(ShopRepository.class).onEnable();
        return false;
    }
}