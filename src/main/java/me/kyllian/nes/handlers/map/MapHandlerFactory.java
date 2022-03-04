package me.kyllian.nes.handlers.map;

import me.kyllian.nes.NESPlugin;
import org.bukkit.Bukkit;

public class MapHandlerFactory {

    private NESPlugin plugin;

    public MapHandlerFactory(NESPlugin plugin) {
        this.plugin = plugin;
    }

    public MapHandler getMapHandler() {
        String minecraftVersion = Bukkit.getVersion();
        int mainVer = Integer.parseInt(minecraftVersion.split("\\.")[1]);
        return mainVer >= 13 ? new MapHandlerNew(plugin) : new MapHandlerOld(plugin);
    }
}
