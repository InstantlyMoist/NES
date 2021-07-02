package com.grapeshot.halfnes;

import com.grapeshot.halfnes.ui.HeadlessUI;
import com.grapeshot.halfnes.ui.PuppetController;

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

    private BufferedImage frame;
    private boolean running = true;

    public HeadlessNES(String romPath)  {
        HeadlessUI ui = new HeadlessUI(romPath,  true);

        while (running) {
            ui.runFrame();
            frame = ui.getLastFrame();
        }
    }

    public void stop() {
        running = false;
    }

    public BufferedImage getFrame() {
        return frame;
    }
}
