package me.kyllian.nes.listeners;

import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private NESPlugin plugin;

    public PlayerMoveListener(NESPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (pocket.isEmpty()) return;
        double diffX = event.getTo().getX() - event.getFrom().getX();
        double diffZ = event.getTo().getZ() - event.getFrom().getZ();
        pocket.getButtonToggleHelper().press(PuppetController.Button.LEFT, diffX > 0.01);
        pocket.getButtonToggleHelper().press(PuppetController.Button.RIGHT, diffX < -0.01);
        pocket.getButtonToggleHelper().press(PuppetController.Button.UP, diffZ > 0.01);
        pocket.getButtonToggleHelper().press(PuppetController.Button.DOWN, diffZ < -0.01);
        event.setTo(event.getFrom());
    }

}
