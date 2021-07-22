package com.areeoh.clans.clans.commands;

import com.areeoh.clans.clans.Clan;
import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.commands.subcommands.*;
import com.areeoh.spigot.framework.Plugin;
import com.areeoh.spigot.framework.commands.Command;
import com.areeoh.spigot.framework.commands.CommandManager;
import com.areeoh.spigot.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClanCmdManager extends CommandManager {

    public ClanCmdManager(Plugin plugin) {
        super(plugin, "ClanCmdManager");
    }

    @Override
    public void registerModules() {
        addModule(new BaseCommand(this));
        addModule(new ClanCreateCommand(this));
        addModule(new ClanDisbandCommand(this));
        addModule(new ClanLeaveCommand(this));
        addModule(new ClanNeutralCommand(this));
        addModule(new ClanAllyCommand(this));
        addModule(new ClanTrustCommand(this));
        addModule(new ClanEnemyCommand(this));
        addModule(new ClanInviteCommand(this));
        addModule(new ClanJoinCommand(this));
        addModule(new ClanKickCommand(this));
        addModule(new ClanClaimCommand(this));
        addModule(new ClanUnclaimCommand(this));
        addModule(new ClanDemoteCommand(this));
        addModule(new ClanPromoteCommand(this));
        addModule(new ClanHomeCommand(this));
        addModule(new ClanSetHomeCommand(this));
        addModule(new ClanMapCommand(this));
    }

    class BaseCommand extends Command<Player> {
        public BaseCommand(CommandManager manager) {
            super(manager, "BaseCommand", Player.class);
            setCommand("clan");
            setAliases("c", "faction", "fac", "f", "gang");
            setIndex(0);
            setRequiredArgs(0);
        }

        @Override
        public boolean execute(Player player, String[] args) {
            final Clan clan = getManager(ClanManager.class).getClan(player);
            if (args == null || args.length == 0) {
                if (clan == null) {
                    UtilMessage.message(player, "Clans", "You are not in a Clan.");
                    return true;
                }
                UtilMessage.message(player, "Clans", getManager(ClanManager.class).getClanRelation(clan, clan).getSuffix() + clan.getName() + ChatColor.GRAY + " Information:");
                UtilMessage.message(player, getManager(ClanManager.class).getClanTooltip(clan, clan));
            } else {
                final Clan target = getManager(ClanManager.class).searchClan(player, args[0], true);
                if (target == null) {
                    return true;
                }
                UtilMessage.message(player, "Clans", getManager(ClanManager.class).getClanRelation(clan, target).getSuffix() + target.getName() + ChatColor.GRAY + " Information:");
                UtilMessage.message(player, getManager(ClanManager.class).getClanTooltip(clan, target));
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, String[] args) {
            final ArrayList<String> strings = new ArrayList<>();
            if (args.length == 1) {
                getModules().forEach(command -> strings.add(command.getCommand()));
                Collections.sort(strings);
                getManager(ClanManager.class).getClanSet().stream().filter(clan -> !clan.isAdmin()).forEach(clan -> {
                    strings.add(clan.getName());
                });
                strings.removeIf(s -> !s.toLowerCase().contains(args[0].toLowerCase()));
            }
            return strings;
        }
    }
}
