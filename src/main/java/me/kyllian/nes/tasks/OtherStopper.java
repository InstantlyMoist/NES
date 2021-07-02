package me.kyllian.nes.tasks;

import com.grapeshot.halfnes.HeadlessNES;
import me.kyllian.nes.NESPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OtherStopper extends BukkitRunnable {

    private long lastUpdate;
    private HeadlessNES emulator;

    public OtherStopper(NESPlugin plugin, HeadlessNES emulator) {
        this.emulator = emulator;
        this.lastUpdate = System.currentTimeMillis();
        runTaskTimer(plugin, 5, 5);
    }

    public void run() {
        if (emulator == null) cancel();
        if (System.currentTimeMillis() - lastUpdate > 100 && emulator != null) {
            //TODO: Stop the following buttons
            /*emulator.buttonA = false;
            emulator.buttonB = false;
            emulator.buttonSelect = false;*/
        }
    }

    public void update() {
        lastUpdate = System.currentTimeMillis();
    }
}
