package com.areeoh.clans.commands.subcommands;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.clans.events.ClanCreateEvent;
import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class ClanCreateCommand extends Command<Player> {

    public ClanCreateCommand(CommandManager manager) {
        super(manager, "ClanCreateCommand", Player.class);
        setCommand("create");
        setIndex(1);
        setRequiredArgs(2);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if(getManager(ClanManager.class).getClan(player.getUniqueId()) != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return false;
        }
        if(getManager(ClanManager.class).getClan(args[1]) != null) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan.");
            return false;
        }
        if(args[1].length() < 3) {
            UtilMessage.message(player, "Clans", "Clan name is too short. Minimum length is " + ChatColor.YELLOW + "3" + ChatColor.GRAY + ".");
            return false;
        }
        if(args[1].length() > 14) {
            UtilMessage.message(player, "Clans", "Clan name is too long. Maximum length is " + ChatColor.YELLOW + "14" + ChatColor.GRAY + ".");
            return false;
        }
        if(Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE).matcher(args[1]).find()) {
            UtilMessage.message(player, "Clans", "You cannot have special characters in your Clan name.");
            return false;
        }
        if(getManager().getModules().stream().map(command -> command.getCommand().toLowerCase()).anyMatch(str -> str.equalsIgnoreCase(args[1]))) {
            UtilMessage.message(player, "Clans", "Clan name cannot be a Clan command.");
            return false;
        }
        //TODO ADD COOLDOWN
        final Clan clan = new Clan(args[1]);
        Bukkit.getPluginManager().callEvent(new ClanCreateEvent(player, clan));
        return true;
    }

    @Override
    public void invalidArgsRequired(Player sender) {
        UtilMessage.message(sender, "Clans", "You did not input a Clan name.");
    }
}