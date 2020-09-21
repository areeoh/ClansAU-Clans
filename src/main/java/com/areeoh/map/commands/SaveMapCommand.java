package com.areeoh.map.commands;

import com.areeoh.framework.commands.Command;
import com.areeoh.framework.commands.CommandManager;
import com.areeoh.map.MapManager;
import org.bukkit.entity.Player;

public class SaveMapCommand extends Command<Player> {

    public SaveMapCommand(CommandManager manager) {
        super(manager, "SaveMapCommand", Player.class);
        setCommand("save");
        setRequiredArgs(1);
        setIndex(1);
    }

    @Override
    public boolean execute(Player player, String[] strings) {
        getManager(MapManager.class).saveMapData();
        return false;
    }
}
