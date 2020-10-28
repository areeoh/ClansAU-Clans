package com.areeoh.clans.fishing;

import com.areeoh.clans.fishing.listeners.FishingListener;
import com.areeoh.clans.fishing.listeners.HandleFishingPlayer;
import com.areeoh.core.framework.Manager;
import com.areeoh.core.framework.Module;
import com.areeoh.core.framework.Plugin;

public class FishingManager extends Manager<Module> {

    public FishingManager(Plugin plugin) {
        super(plugin, "FishingManager");
    }

    @Override
    public void registerModules() {
        addModule(new FishingListener(this));
        addModule(new HandleFishingPlayer(this));
    }
}
