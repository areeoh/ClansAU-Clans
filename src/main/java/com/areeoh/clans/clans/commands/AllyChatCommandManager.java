package com.areeoh.clans.clans.commands;

import com.areeoh.clans.clans.ChatType;
import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AllyChatCommandManager extends CommandManager {

    public AllyChatCommandManager(Plugin plugin) {
        super(plugin, "AllyChatCommandManager");
    }

    @Override
    public void registerModules() {
        addModule(new BaseCommand(this));
    }

    class BaseCommand extends Command<Player> {

        public BaseCommand(CommandManager manager) {
            super(manager, "BaseCommand", Player.class);
            setCommand("allychat");
            setAliases("ac");
            setIndex(0);
        }

        @Override
        public boolean execute(Player player, String[] args) {
            Clan clan = getManager(ClanManager.class).getClan(player);
            if (clan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return false;
            }
            if(args == null || args.length == 0) {
                if(getManager(ClanManager.class).getChatMap().getOrDefault(player.getUniqueId(), ChatType.NONE) == ChatType.ALLY) {
                    UtilMessage.message(player, "Ally Chat", "Ally Chat: " + ChatColor.RED + "Disabled");
                    getManager(ClanManager.class).getChatMap().remove(player.getUniqueId());
                } else {
                    UtilMessage.message(player, "Ally Chat", "Ally Chat: " + ChatColor.GREEN + "Enabled");
                    getManager(ClanManager.class).getChatMap().put(player.getUniqueId(), ChatType.ALLY);
                }
                return true;
            }
            String msg = UtilMessage.getFinalArg(args, 0);
            for (String s : clan.getAllianceMap().keySet()) {
                getManager(ClanManager.class).getClan(s).inform(false, "", ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + msg);
            }
            clan.inform(false, "", ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + msg);
            return false;
        }
    }
}
