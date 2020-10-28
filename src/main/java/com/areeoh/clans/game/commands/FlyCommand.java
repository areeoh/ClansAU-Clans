package com.areeoh.clans.game.commands;

import com.areeoh.core.ClansAUCore;
import com.areeoh.core.framework.commands.Command;
import com.areeoh.core.framework.commands.CommandManager;
import org.bukkit.entity.Player;

public class FlyCommand extends CommandManager {

    public FlyCommand(ClansAUCore plugin) {
        super(plugin, "FlyCommand");
    }

    @Override
    public void registerModules() {
        addModule(new Command<Player>(this, "Fly", Player.class) {
            @Override
            public boolean execute(Player sender, String[] args) {
                sender.setFlySpeed(Float.parseFloat(args[0]));
                return true;
            }
        }.setCommand("fly").setIndex(0).setRequiredArgs(1));
    }
}