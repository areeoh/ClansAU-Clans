package com.areeoh.fishing;

import com.areeoh.ClansAUCore;
import com.areeoh.framework.Manager;
import com.areeoh.framework.Module;
import com.areeoh.fishing.listeners.FishingListener;
import com.areeoh.fishing.listeners.HandleFishingPlayer;

public class FishingManager extends Manager<Module> {

    public FishingManager(ClansAUCore plugin) {
        super(plugin, "FishingManager");
    }

    @Override
    public void registerModules() {
        addModule(new FishingListener(this));
        addModule(new HandleFishingPlayer(this));
    }
}
