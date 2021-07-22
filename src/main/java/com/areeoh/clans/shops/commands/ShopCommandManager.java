package com.areeoh.clans.shops.commands;

import com.areeoh.spigot.client.Rank;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import org.bukkit.entity.Player;

public class ShopCommandManager extends CommandManager {

    public ShopCommandManager(Plugin plugin) {
        super(plugin, "ShopCommandManager");
    }

    @Override
    public void registerModules() {
        addModule(new BaseCommand(this));
        addModule(new ShopSpawnCommand(this));
    }

    class BaseCommand extends Command<Player> {

        public BaseCommand(CommandManager manager) {
            super(manager, "BaseCommand", Player.class);
            setCommand("shop");
            setAliases("shops");
            setIndex(0);
            setRequiredRank(Rank.OWNER);
            setRequiredArgs(1);
        }

        @Override
        public boolean execute(Player player, String[] strings) {
            return false;
        }
    }
}