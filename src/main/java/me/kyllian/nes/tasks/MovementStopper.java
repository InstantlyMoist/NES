package me.kyllian.nes.tasks;

import com.grapeshot.halfnes.HeadlessNES;
import me.kyllian.nes.NESPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MovementStopper extends BukkitRunnable {

    private long lastUpdate;
    private HeadlessNES emulator;

    public MovementStopper(NESPlugin plugin, HeadlessNES emulator) {
        this.emulator = emulator;
        this.lastUpdate = System.currentTimeMillis();
        runTaskTimer(plugin, 5, 5);
    }

    public void run() {
        if (emulator == null) cancel();
        if (System.currentTimeMillis() - lastUpdate > 100 && emulator != null) {
            //TODO: Reset the following buttons
            /*emulator.buttonUp = false;
            emulator.buttonDown = false;
            emulator.buttonLeft = false;
            emulator.buttonRight = false;*/
        }
    }

    public void update() {
        lastUpdate = System.currentTimeMillis();
    }
}
