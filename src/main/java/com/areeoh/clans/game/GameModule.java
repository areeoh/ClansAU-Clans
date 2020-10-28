package com.areeoh.clans.game;


import com.areeoh.core.framework.Module;

public abstract class GameModule extends Module<GameManager> {

    public GameModule(GameManager manager, String moduleName) {
        super(manager, moduleName);
    }
}
