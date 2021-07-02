package me.kyllian.nes.listeners;

import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsListener implements Listener {

    private NESPlugin plugin;

    public PlayerSwapHandItemsListener(NESPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (pocket.isEmpty()) return;
        event.setCancelled(true);
        pocket.getButtonToggleHelper().press(PuppetController.Button.START, true);
    }
}
