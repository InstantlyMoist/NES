package me.kyllian.nes.commands;

import me.kyllian.nes.NESPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class CommandManager {

    private final NESPlugin plugin;
    private final CommandMap commandMap;

    public CommandManager(@NotNull NESPlugin plugin) {
        this.plugin = plugin;
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            this.commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            bukkitCommandMap.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerDefaults() {
        registerCommand(new NESCommand(plugin));
    }

    public void reload() {
        BaseCommand.helpComponent = null;
    }

    public void registerCommand(@NotNull BaseCommand command) {
        this.commandMap.register(plugin.getName(), command);
    }

}
