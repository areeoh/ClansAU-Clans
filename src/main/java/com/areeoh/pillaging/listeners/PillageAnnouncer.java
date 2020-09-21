package com.areeoh.pillaging.listeners;

import com.areeoh.framework.Module;
import com.areeoh.framework.updater.Update;
import com.areeoh.framework.updater.Updater;
import com.areeoh.utility.UtilTime;
import com.areeoh.pillaging.Pillage;
import com.areeoh.pillaging.PillageManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class PillageAnnouncer extends Module<PillageManager> implements Updater {

    public PillageAnnouncer(PillageManager manager) {
        super(manager, "PillageAnnouncer");
    }

    @Update()
    public void onUpdate() {
        if(getManager().getPillages().isEmpty()) {
            return;
        }
        for (Pillage pillage : getManager().getPillages()) {
            if (UtilTime.elapsed(pillage.getLastAnnounce(), 60000L)) {
                final String remainingString = UtilTime.getTime2(pillage.getTimeRemaining(), UtilTime.TimeUnit.MINUTES, 1);
                pillage.getPillagee().inform(true, "Clans", "The Pillage on your clan ends in " + ChatColor.GREEN + remainingString + ChatColor.GRAY + ".", null);
                pillage.getPillager().inform(true, "Clans", "The Pillage on " + ChatColor.LIGHT_PURPLE + "Clan " + pillage.getPillagee().getName() + ChatColor.GRAY + " ends in " + ChatColor.GREEN + remainingString + ChatColor.GRAY + ".", null);
                pillage.setLastAnnounce(System.currentTimeMillis());

                pillage.getPillagee().playSound(Sound.NOTE_PLING, 0.5F, 1.2F);
                pillage.getPillager().playSound(Sound.NOTE_PLING, 0.5F, 1.2F);
            }
        }
    }
}