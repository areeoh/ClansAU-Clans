package com.areeoh.map.data;

import com.areeoh.map.nms.MaterialMapColorInterface;
import com.areeoh.map.renderer.MinimapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;

public class QueueHandler extends BukkitRunnable {

    private final MinimapRenderer minimapRenderer;

    public QueueHandler(MinimapRenderer minimapRenderer) {
        this.minimapRenderer = minimapRenderer;
    }

    @Override
    public void run() {
        final Queue<Coords> queue = this.minimapRenderer.getQueue();

        if(queue.isEmpty()) {
            return;
        }

        for (int i = 0; i < 64; i++) {
            final Coords poll = queue.poll();

            if(poll == null) {
                break;
            }

            World world = Bukkit.getWorld(poll.getWorld());

            if(!minimapRenderer.getWorldCacheMap().containsKey(poll.getWorld())) {
                continue;
            }
            if(!minimapRenderer.getWorldCacheMap().get(poll.getWorld()).containsKey(poll.getX())) {
                continue;
            }
            if(!minimapRenderer.getWorldCacheMap().get(poll.getWorld()).get(poll.getX()).containsKey(poll.getZ())) {
                continue;
            }
            Block b = world.getBlockAt(poll.getX(), world.getHighestBlockYAt(poll.getX(), poll.getZ()), poll.getZ());

            if (!b.getChunk().isLoaded()) {
                continue;
            }
            while (b.getY() > 0 && minimapRenderer.getNms().getBlockColor(b) == minimapRenderer.getNms().getColorNeutral()) {
                b = world.getBlockAt(b.getX(), b.getY() - 1, b.getZ());
            }
            short avgY = 0;
            avgY += b.getY();

            MaterialMapColorInterface mainColor = minimapRenderer.getNms().getBlockColor(b);

            final MapPixel mapPixel = minimapRenderer.getWorldCacheMap().get(b.getWorld().getName()).get(b.getX()).get(b.getZ());
            mapPixel.setAverageY(avgY);
            mapPixel.setColor((byte) (mainColor.getM() * 4));
        }
    }
}