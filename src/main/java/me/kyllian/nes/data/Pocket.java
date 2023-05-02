package me.kyllian.nes.data;

import com.grapeshot.halfnes.HeadlessNES;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.entity.RideableArmorstand;
import me.kyllian.nes.helpers.ButtonToggleHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Pocket {

    private NESPlugin plugin;

    private HeadlessNES emulator;

    private ButtonToggleHelper buttonToggleHelper;

    private ItemStack handItem = null;
    private RideableArmorstand rideableArmorstand;
    private Entity companionArrow;
    private Player companion;

    public void loadEmulator(NESPlugin plugin, Player player, String path) { //TODO Add file path here
        this.plugin = plugin;

        emulator = new HeadlessNES(plugin, path);

        buttonToggleHelper = new ButtonToggleHelper(plugin, emulator);

        handItem = player.getInventory().getItemInMainHand();
    }

    public void stopEmulator(Player player) {
        rideableArmorstand.remove();
        player.getInventory().setItemInMainHand(handItem);
        handItem = null;

        emulator.stop();

        buttonToggleHelper.cancel();

        emulator = null;
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

    public void setCompanionArrow(Entity companionArrow) {
        this.companionArrow = companionArrow;
    }

    public void setRideableArmorstand(RideableArmorstand rideableArmorstand) {
        this.rideableArmorstand = rideableArmorstand;
    }
}
