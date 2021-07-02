package me.kyllian.nes.listeners;

import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private NESPlugin plugin;

    public PlayerDropItemListener(NESPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (!pocket.isEmpty())  {
            pocket.getButtonToggleHelper().press(PuppetController.Button.SELECT, true);
            event.setCancelled(true);
        }
    }
}
