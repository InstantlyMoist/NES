package me.kyllian.nes.listeners;

import me.kyllian.nes.GameboyPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldListener implements Listener {

    private GameboyPlugin plugin;

    public PlayerItemHeldListener(GameboyPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,  plugin);
    }

    @EventHandler
    public void on(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (!pocket.isEmpty()) event.setCancelled(true);
    }
}
