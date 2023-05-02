package me.kyllian.nes;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.kyllian.nes.commands.CommandManager;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.handlers.MessageHandler;
import me.kyllian.nes.handlers.PlayerHandler;
import me.kyllian.nes.handlers.RomHandler;
import me.kyllian.nes.handlers.map.MapHandler;
import me.kyllian.nes.handlers.map.MapHandlerFactory;
import me.kyllian.nes.listeners.manager.PacketListenerManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class NESPlugin extends JavaPlugin {

    private int gamesEmulated = 0;
    private MapHandler mapHandler;
    private MessageHandler messageHandler;
    private PlayerHandler playerHandler;
    private RomHandler romHandler;
    private PacketListenerManager packetListenerManager;
    private CommandManager commandManager;


    @Override
    public void onLoad() {
        PacketEventsSettings settings = new PacketEventsSettings()
                .bStats(false)
                .checkForUpdates(false);
        PacketEvents.setAPI(
                SpigotPacketEventsBuilder.build(
                        this,
                        settings
                )
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PacketEvents.getAPI().init();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        mapHandler = new MapHandlerFactory(this).getMapHandler();
        messageHandler = new MessageHandler(this);
        playerHandler = new PlayerHandler(this);
        romHandler = new RomHandler(this);
        packetListenerManager = new PacketListenerManager(this);
        commandManager = new CommandManager(this);

        Metrics metrics = new Metrics(this, 11908);
        metrics.addCustomChart(new Metrics.SingleLineChart("games_emulated", () ->
                gamesEmulated));
        metrics.addCustomChart(new Metrics.AdvancedPie("games_installed", () -> {
            Map<String, Integer> values = new HashMap<>();
            romHandler.getRoms().keySet().forEach(romName -> {
                values.put(romName, 1);
            });
            return values;
        }));
        mapHandler.loadData();
        commandManager.registerDefaults();
        commandManager.reload();
        packetListenerManager.setup();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Pocket pocket = playerHandler.getPocket(player);
            if (!pocket.isEmpty()) pocket.stopEmulator(player);
        });
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public RomHandler getRomHandler() {
        return romHandler;
    }

    public void notifyEmulate() {
        gamesEmulated++;
    }


}
