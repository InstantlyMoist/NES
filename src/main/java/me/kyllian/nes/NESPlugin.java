package me.kyllian.nes;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.kyllian.nes.commands.NesExecutor;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.handlers.MessageHandler;
import me.kyllian.nes.handlers.PlayerHandler;
import me.kyllian.nes.handlers.RomHandler;
import me.kyllian.nes.handlers.map.MapHandler;
import me.kyllian.nes.handlers.map.MapHandlerFactory;
import me.kyllian.nes.listeners.*;
import me.kyllian.nes.listeners.packets.SteerVehicleListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class NESPlugin extends JavaPlugin {

    private int gamesEmulated = 0;
    private MapHandler mapHandler;
    private MessageHandler messageHandler;
    private PlayerHandler playerHandler;
    private RomHandler romHandler;


    @Override
    public void onLoad() {
        PacketEventsSettings settings = new PacketEventsSettings();
        settings.bStats(false);
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

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        mapHandler = new MapHandlerFactory(this).getMapHandler();
        messageHandler = new MessageHandler(this);
        playerHandler = new PlayerHandler(this);
        romHandler = new RomHandler(this);

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

        getCommand("nes").setExecutor(new NesExecutor(this));

        new InventoryClickListener(this);
        new PlayerDropItemListener(this);
        new PlayerInteractEntityListener(this);
        new PlayerInteractListener(this);
        new PlayerItemHeldListener(this);
        new PlayerQuitListener(this);
        new PlayerSwapHandItemsListener(this);
        PacketEvents.getAPI().getEventManager().registerListener(new SteerVehicleListener(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();

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
