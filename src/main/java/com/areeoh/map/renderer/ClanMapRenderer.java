package com.areeoh.map.renderer;

import com.areeoh.clans.Clan;
import com.areeoh.clans.ClanManager;
import com.areeoh.map.MapManager;
import com.areeoh.map.data.ChunkData;
import com.areeoh.map.data.MapSettings;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class ClanMapRenderer extends MapRenderer {

    private final MapManager mapManager;

    public ClanMapRenderer(MapManager mapManager) {
        super(true);
        this.mapManager = mapManager;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (!(player.getItemInHand() != null && player.getItemInHand().getType() == Material.MAP)) {
            return;
        }

        final MapSettings mapSettings = mapManager.mapSettingsMap.get(player.getUniqueId());
        MapSettings.Scale s = mapSettings.getScale();

        final boolean hasMoved = mapManager.hasMoved(player);
        if(!(hasMoved || mapSettings.isUpdate())) {
            return;
        }

        final MapCursorCollection cursors = mapCanvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }

        int scale = 1 << s.getValue();

        int centerX = player.getLocation().getBlockX();
        int centerZ = player.getLocation().getBlockZ();

        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                mapCanvas.setPixel(i, j, mapCanvas.getBasePixel(i, j));
            }
        }

        if (s == MapSettings.Scale.FAR) {
            centerX = 0;
            centerZ = 0;
        }

        final ClanManager manager = mapManager.getManager(ClanManager.class);

        for (ChunkData chunkData : mapManager.clanMapData.get(player.getUniqueId())) {
            if(!chunkData.getWorld().equals(player.getWorld().getName())) {
                continue;
            }
            final Clan clan = manager.getClan(chunkData.getClan());
            if(clan == null) {
                continue;
            }

            int bx = chunkData.getX() << 4; //Chunk's actual world coord;
            int bz = chunkData.getZ() << 4; //Chunk's actual world coord;

            int pX = MathHelper.floor(bx - centerX) / scale + 64; //Gets the pixel location;
            int pZ = MathHelper.floor(bz - centerZ) / scale + 64; //Gets the pixel location;

            final boolean admin = clan.isAdmin();
            for (int cx = 0; cx < 16 / scale; cx++) {
                for (int cz = 0; cz < 16 / scale; cz++) {
                    if (pX + cx >= 0 && pX + cx < 128 && pZ + cz >= 0 && pZ + cz < 128) { //Checking if its in the maps bounds;
                        if (s.ordinal() <= MapView.Scale.CLOSE.ordinal() || admin) {
                            mapCanvas.setPixel(pX + cz, pZ + cz, chunkData.getColor());
                        }
                        if (s.ordinal() < MapView.Scale.NORMAL.ordinal() || admin) {
                            if (cx == 0) {
                                if (!chunkData.getBlockFaceSet().contains(BlockFace.WEST)) {
                                    mapCanvas.setPixel(pX + cx, pZ + cz, chunkData.getColor());
                                }
                            }
                            if (cx == (16 / scale) - 1) {
                                if (!chunkData.getBlockFaceSet().contains(BlockFace.EAST)) {
                                    mapCanvas.setPixel(pX + cx, pZ + cz, chunkData.getColor());
                                }
                            }
                            if (cz == 0) {
                                if (!chunkData.getBlockFaceSet().contains(BlockFace.NORTH)) {
                                    mapCanvas.setPixel(pX + cx, pZ + cz, chunkData.getColor());
                                }
                            }
                            if (cz == (16 / scale) - 1) {
                                if (!chunkData.getBlockFaceSet().contains(BlockFace.SOUTH)) {
                                    mapCanvas.setPixel(pX + cx, pZ + cz, chunkData.getColor());
                                }
                            }
                        } else {
                            mapCanvas.setPixel(pX + cx, pZ + cz, chunkData.getColor());
                        }
                    }
                }
            }
        }
        mapManager.updateLastMoved(player);
        mapSettings.setUpdate(false);
    }
}