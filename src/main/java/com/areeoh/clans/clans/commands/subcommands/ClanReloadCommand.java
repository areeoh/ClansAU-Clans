package com.areeoh.clans.clans.commands.subcommands;

import com.areeoh.clans.clans.ClanManager;
import com.areeoh.clans.clans.ClanRepository;
import com.areeoh.core.client.Rank;
import com.areeoh.core.database.DatabaseManager;
import com.areeoh.core.framework.commands.Command;
import com.areeoh.core.framework.commands.CommandManager;
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
