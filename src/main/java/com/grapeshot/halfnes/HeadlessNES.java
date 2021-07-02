package com.grapeshot.halfnes;

import com.grapeshot.halfnes.ui.HeadlessUI;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/**
 * @author Mitchell Skaggs
 */
public class HeadlessNES {

    private HeadlessUI ui;

    private BufferedImage frame = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
    private boolean running = true;

    public HeadlessNES(NESPlugin plugin, String romPath) {
        ui = new HeadlessUI(romPath, true);
        new BukkitRunnable() {
            public void run() {
                if (!running) {
                    ui.getNes().quit();
                    cancel();
                    return;
                }
                while (running) {
                    ui.runFrame();
                    frame = ui.getLastFrame();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void stop() {
        running = false;
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public HeadlessUI getUi() {
        return ui;
    }
}
