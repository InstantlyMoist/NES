package me.kyllian.nes.handlers;

import me.kyllian.nes.NESPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RomHandler {

    private NESPlugin plugin;

    private Map<String, String> roms;
    private File romFolder;

    public RomHandler(NESPlugin plugin) {
        this.plugin = plugin;

        romFolder = new File(plugin.getDataFolder(), "roms");
        if (!romFolder.exists()) romFolder.mkdirs();

        try {
            loadRoms();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadRoms() throws IOException {
        roms = new HashMap<>();

        for (File rom : romFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.endsWith(".nes"));
            }
        })) {
            roms.put(rom.getName(), rom.toPath().toString());
        }
    }

    public Map<String, String> getRoms() {
        return roms;
    }
}
