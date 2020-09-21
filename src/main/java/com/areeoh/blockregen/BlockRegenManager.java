package com.areeoh.blockregen;

import com.areeoh.blockregen.listeners.BlockRegenHandler;
import com.areeoh.ClansAUCore;
import com.areeoh.framework.Manager;
import com.areeoh.framework.Module;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockRegenManager extends Manager<Module> {

    public BlockRegenManager(ClansAUCore plugin) {
        super(plugin, "BlockRegenManager");
    }

    @Override
    public void registerModules() {
        addModule(new BlockRegenHandler(this));
    }

    @SuppressWarnings("deprecation")
    public void outlineChunk(Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (z == 0 || z == 15 || x == 0 || x == 15) {
                    Block down = chunk.getWorld().getHighestBlockAt(chunk.getBlock(x, 0, z).getLocation()).getRelative(BlockFace.DOWN);
                    if (down.getTypeId() == 1 || down.getTypeId() == 2 || down.getTypeId() == 3 || down.getTypeId() == 12 || down.getTypeId() == 8) {
                        getModule(BlockRegenHandler.class).addBlock(down, Material.GLOWSTONE, (byte) 0, 300000L);
                    }
                }
            }
        }
    }
}