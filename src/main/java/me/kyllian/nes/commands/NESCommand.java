package me.kyllian.nes.commands;

import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.commands.sub.PlayCommand;
import me.kyllian.nes.commands.sub.ReloadCommand;
import me.kyllian.nes.commands.sub.StopCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class NESCommand extends BaseCommand {

    public NESCommand(@NotNull NESPlugin plugin) {
        super(
                plugin,
                "nes",
                "The base NES emulator command",
                "/nes <subcommand>",
                Collections.emptyList(),
                null
                );
        addSubCommand(new PlayCommand(plugin));
        addSubCommand(new ReloadCommand(plugin));
        addSubCommand(new StopCommand(plugin));
    }

    @Override
    public void handle(@NotNull Player sender, @NotNull String[] args) {
        showHelp(sender);
    }


}
