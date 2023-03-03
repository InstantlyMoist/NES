package me.kyllian.nes.helpers;

import com.grapeshot.halfnes.HeadlessNES;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ButtonToggleHelper extends BukkitRunnable {

    private final HeadlessNES emulator;

    private final Map<PuppetController.Button, Long> buttonLastPressTimes;
    private final int buttonDebounce;

    public ButtonToggleHelper(NESPlugin plugin, HeadlessNES emulator) {
        this.emulator = emulator;
        buttonLastPressTimes = new HashMap<>();

        buttonDebounce = plugin.getConfig().getInt("button_debounce");

        runTaskTimerAsynchronously(plugin, 5, 5);
    }

    public void press(PuppetController.Button button, Boolean state) {
        if (state) buttonLastPressTimes.put(button, System.currentTimeMillis());
        emulator.getUi().getController1().pressButton(PuppetController.Button.START);

        if (state) emulator.getUi().getController1().pressButton(button);
        else emulator.getUi().getController1().releaseButton(button);
    }

    @Override
    public void run() {
        Iterator buttonIterator = buttonLastPressTimes.keySet().iterator();
        while (buttonIterator.hasNext()) {
            PuppetController.Button button = (PuppetController.Button) buttonIterator.next();
            long lastPressTime = buttonLastPressTimes.get(button);
            if (System.currentTimeMillis() - lastPressTime > buttonDebounce) {
                press(button, false);
                buttonIterator.remove();
            }
        }
    }
}
