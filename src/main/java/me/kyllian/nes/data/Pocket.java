package me.kyllian.nes.data;

import com.grapeshot.halfnes.HeadlessNES;
import me.kyllian.nes.GameboyPlugin;
import me.kyllian.nes.helpers.ButtonToggleHelper;
import nitrous.Cartridge;
import nitrous.cpu.Emulator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pocket {

    private BukkitTask arrowDespawnHandler;

    private GameboyPlugin plugin;

    private HeadlessNES emulator;

    private ButtonToggleHelper buttonToggleHelper;

    private ItemStack handItem = null;
    private Entity arrow;

    public void loadEmulator(GameboyPlugin plugin, Player player) {
        this.plugin = plugin;

        emulator = new HeadlessNES(plugin.getDataFolder() + "/game.nes");

        //buttonToggleHelper = new ButtonToggleHelper(plugin, emulator);

        handItem = player.getInventory().getItemInMainHand();

        arrowDespawnHandler = new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow != null) arrow.setTicksLived(1);
            }
        }.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public void stopEmulator(Player player) {
        if (plugin.isProtocolLib()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    arrow.setTicksLived(10000);
                    arrow.remove();
                }
            }.runTask(plugin);
        }
        arrow = null;

        player.getInventory().setItemInMainHand(handItem);
        handItem = null;

        emulator.stop();

        buttonToggleHelper.cancel();

        emulator = null;

        arrowDespawnHandler.cancel();
        arrowDespawnHandler = null;
    }

    public boolean isEmpty() {
        return emulator == null;
    }

    public HeadlessNES getEmulator() {
        return emulator;
    }

    public ButtonToggleHelper getButtonToggleHelper() {
        return buttonToggleHelper;
    }

    public Entity getArrow() {
        return arrow;
    }

    public void setArrow(Entity arrow) {
        this.arrow = arrow;
    }
}
