package me.kyllian.nes.data;

import com.grapeshot.halfnes.HeadlessNES;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.helpers.ButtonToggleHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Pocket {

    private BukkitTask arrowDespawnHandler;

    private NESPlugin plugin;

    private HeadlessNES emulator;

    private ButtonToggleHelper buttonToggleHelper;

    private ItemStack handItem = null;
    private Entity arrow;
    private Entity companionArrow;
    private Player companion;

    public void loadEmulator(NESPlugin plugin, Player player, String path) { //TODO Add file path here
        this.plugin = plugin;

        emulator = new HeadlessNES(plugin, path);

        buttonToggleHelper = new ButtonToggleHelper(plugin, emulator);

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
           try {
               new BukkitRunnable() {
                   public void run() {
                       arrow.remove();
                       arrow = null;
                   }
               }.runTask(plugin);
           } catch (Exception exception) {
               arrow.remove();
               arrow = null;
           }
        }

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

    public void setArrow(Entity arrow) {
        this.arrow = arrow;
    }

    public void setCompanionArrow(Entity companionArrow) {
        this.companionArrow = companionArrow;
    }
}
