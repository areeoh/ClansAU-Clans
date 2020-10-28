package com.areeoh.clans.map.commands;

import com.areeoh.core.framework.Plugin;
import com.areeoh.core.framework.commands.Command;
import com.areeoh.core.framework.commands.CommandManager;
import com.areeoh.core.utility.UtilItem;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MapCommandManager extends CommandManager {
    public MapCommandManager(Plugin plugin) {
        super(plugin, "MapCommandManager");
    }

    @Override
    public void registerModules() {
        addModule(new BaseCommand(this));
        addModule(new SaveMapCommand(this));
        addModule(new LoadMapCommand(this));
    }

    class BaseCommand extends Command<Player> {
        public BaseCommand(CommandManager manager) {
            super(manager, "BaseCommand", Player.class);
            setCommand("map");
            setIndex(0);
            setRequiredArgs(0);
        }

        @Override
        public boolean execute(Player player, String[] args) {
            if (player.getInventory().contains(Material.MAP)) {
                UtilMessage.message(player, "Clans", "You already have a map in your inventory.");
                return false;
            }
            if (player.getInventory().firstEmpty() == -1) {
                UtilMessage.message(player, "Clans", "You do not have enough space in your inventory for a map.");
                return false;
            }
            ItemStack is = new ItemStack(Material.MAP);
            is.setDurability((short) 0);
            player.getInventory().addItem(UtilItem.updateNames(is));

            UtilMessage.message(player, "Clans", "A map was added to your inventory.");
            return true;
        }
    }
}
