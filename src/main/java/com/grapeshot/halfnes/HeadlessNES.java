package com.grapeshot.halfnes;

import com.grapeshot.halfnes.ui.FrameLimiterImpl;
import com.grapeshot.halfnes.ui.FrameLimiterInterface;
import com.grapeshot.halfnes.ui.HeadlessUI;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.makers.ThumbnailMaker;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;
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

    private final HeadlessUI ui;

    private final long framesPerNano = 16666666;

    public long lastFrameTime = 0;

    private BufferedImage frame = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
    public byte[] freeBufferArrayByte = new byte[23040];
    private boolean running = true;

    private final Resizer resizer = DefaultResizerFactory.getInstance().getResizer(new Dimension(160, 144), new Dimension(128, 128));
    private final ThumbnailMaker thumbnailMaker = new FixedSizeThumbnailMaker(128, 128, true, true).resizer(resizer);

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
                    boolean canRun = lastFrameTime + framesPerNano < System.nanoTime();
                    if (!canRun) continue;
                    ui.runFrame();

                    frame = thumbnailMaker.make(ui.getLastFrame());
                    for (int x = 0; x < frame.getWidth(); x++) {
                        for (int y = 0; y < frame.getHeight(); y++) {
                            Color color = new Color(frame.getRGB(x, y));
                            freeBufferArrayByte[y * 128 + x] = MapPalette.matchColor(color);
                        }
                    }
                    lastFrameTime = System.nanoTime();
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
