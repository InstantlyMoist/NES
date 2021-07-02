package me.kyllian.nes.listeners;

import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private NESPlugin plugin;

    public PlayerInteractListener(NESPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (pocket.isEmpty()) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            pocket.getButtonToggleHelper().press(PuppetController.Button.B, true);
        event.setCancelled(true);
    }
}
