package com.areeoh.clans.game.listeners;

import com.areeoh.clans.game.GameManager;
import com.areeoh.clans.game.GameModule;
import com.areeoh.core.framework.updater.Update;
import com.areeoh.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

public class BroadcastTips extends GameModule implements Listener {

    public BroadcastTips(GameManager manager) {
        super(manager, "BroadcastTips");
    }

    private int count = 0;

    private final String[] tips = new String[]{
            ChatColor.GRAY + "Join our Discord - " + ChatColor.GREEN + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "https://clansau.net/discord",
            ChatColor.GRAY + "Vote daily to receive a " + ChatColor.YELLOW + "Voting Crate " + ChatColor.GRAY + " (Win up to 50k, TNT and more!)" + ChatColor.YELLOW + "(/vote)",
            ChatColor.GRAY + "Visit our website - " + ChatColor.GREEN + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "https://clansau.net",
            ChatColor.GRAY + "Don't forget to complete your daily quests. " + ChatColor.YELLOW + "(/daily)",
            ChatColor.GRAY + "Not sure where Shops are located? Type " + ChatColor.YELLOW + "(/coords)" + ChatColor.GRAY + " for help",
            ChatColor.GRAY + "The farming levels are 44 to 60 Y",
            ChatColor.GRAY + "The server is currently in beta, If you experience any bugs please report them to staff",
            ChatColor.GRAY + "Receive double online rewards by using the CAH client " + ChatColor.YELLOW + "https://clansau.net/CAH"
    };

    @Update(ticks = 18000)
    public void onBroadcastTip() {
        if (count >= tips.length - 1) {
            count = 0;
        }
        Bukkit.getOnlinePlayers().forEach(o -> o.playSound(o.getLocation(), Sound.ORB_PICKUP, 1.0F, 2.0F));
        UtilMessage.broadcast("Tips", tips[count]);
        count++;
    }
}
