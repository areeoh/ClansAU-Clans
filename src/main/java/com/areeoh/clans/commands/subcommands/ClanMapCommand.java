package com.areeoh.clans.commands.subcommands;

import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.utility.UtilItem;
import com.areeoh.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClanMapCommand extends Command<Player> {

    public ClanMapCommand(CommandManager manager) {
        super(manager, "ClanMapCommand", Player.class);
        setCommand("map");
        setIndex(1);
        setRequiredArgs(1);
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
