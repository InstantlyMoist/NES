package me.kyllian.nes.commands.sub;

import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.commands.BaseCommand;
import me.kyllian.nes.data.Pocket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class StopCommand extends BaseCommand {

    public StopCommand(@NotNull NESPlugin plugin) {
        super(
                plugin,
                "stop",
                "Stop the currently running game",
                "/nes stop",
                Collections.emptyList(),
                null
        );
    }

    @Override
    public void handle(@NotNull Player sender, @NotNull String[] args) {
        Pocket pocket = plugin.getPlayerHandler().getPocket(sender);
        if (pocket.isEmpty()) {
            sendMessage(sender, "no-game");
            return;
        }
        pocket.stopEmulator(sender);
        sendMessageKey(sender, "stopped");
    }
}
