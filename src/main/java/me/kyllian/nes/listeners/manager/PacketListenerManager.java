package me.kyllian.nes.listeners.manager;

import com.github.retrooper.packetevents.PacketEvents;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.listeners.*;
import me.kyllian.nes.listeners.bukkit.BukkitListeners;

/**
 * TODO: Refactor more bukkit listeners to use packets instead
 */
public class PacketListenerManager {

    private final NESPlugin plugin;

    public PacketListenerManager(NESPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        plugin.getLogger().info("Registering listeners...");
        registerListeners(
                SteerVehicleListener.class,
                DisconnectListener.class
        );
        new BukkitListeners(plugin);
        plugin.getLogger().info("Registered listeners!");
    }

    public void registerListeners(Class<? extends PacketListener>... listeners) {
        for (Class<? extends PacketListener> listener : listeners) {
            registerListener(listener);
        }
    }

    public void registerListener(Class<? extends PacketListener> classes) {
        PacketListener listener = null;
        try {
            listener = classes.getConstructor(NESPlugin.class).newInstance(plugin);
        }
        catch (Exception ignored) {}
        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

}
