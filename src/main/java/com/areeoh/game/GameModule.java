package com.areeoh.game;

import com.areeoh.framework.Module;

public abstract class GameModule extends Module<GameManager> {

    public GameModule(GameManager manager, String moduleName) {
        super(manager, moduleName);
    }
}
