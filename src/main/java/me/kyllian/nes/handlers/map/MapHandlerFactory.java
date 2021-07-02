package me.kyllian.nes.handlers.map;

import me.kyllian.nes.GameboyPlugin;
import org.bukkit.Bukkit;

public class MapHandlerFactory {

    private GameboyPlugin plugin;

    public MapHandlerFactory(GameboyPlugin plugin) {
        this.plugin = plugin;
    }

    public MapHandler getMapHandler() {
        String version = Bukkit.getVersion();
        if (version.contains("1.17") || version.contains("1.16") || version.contains("1.15") || version.contains("1.14") || version.contains("1.13")) return new MapHandlerNew(plugin);
        else return new MapHandlerOld(plugin);
    }
}