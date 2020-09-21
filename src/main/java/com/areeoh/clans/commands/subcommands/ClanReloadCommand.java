package com.areeoh.clans.commands.subcommands;

import com.areeoh.clans.ClanManager;
import com.areeoh.clans.ClanRepository;
import com.areeoh.client.Rank;
import com.areeoh.database.DatabaseManager;
import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClanReloadCommand extends Command<Player> {

    public ClanReloadCommand(CommandManager manager) {
        super(manager, "ClanReloadCommand", Player.class);
        setCommand("reload");
        setIndex(1);
        setRequiredArgs(1);
        setRequiredRank(Rank.OWNER);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        getManager(ClanManager.class).getClanSet().clear();
        new BukkitRunnable() {
            public void run() {
                getManager(DatabaseManager.class).getModule(ClanRepository.class).loadClans();
            }
        }.runTaskAsynchronously(getPlugin());
        return true;
    }
}
