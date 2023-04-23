package me.kyllian.nes.commands.sub;

import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.commands.BaseCommand;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.entity.RideableArmorstand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayCommand extends BaseCommand {

    public PlayCommand(@NotNull NESPlugin plugin) {
        super(
                plugin,
                "play",
                "Play a game",
                "/nes play <game>",
                Collections.emptyList(),
                "nes.play"
        );
    }

    @Override
    public void handle(@NotNull Player sender, @NotNull String[] args) {
        Pocket pocket = plugin.getPlayerHandler().getPocket(sender);
        if (!pocket.isEmpty()) {
            sendMessageKey(sender, "already-running");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(" ");
        }
        String game = sb.toString().trim();
        String foundPath = plugin.getRomHandler().getRoms().get(game);
        if (foundPath == null) {
            sendMessageKey(sender, "not-found");
            showHelp(sender);
            return;
        }
        RideableArmorstand r = new RideableArmorstand(sender, plugin);
        pocket.setRideableArmorstand(r);
        sender.sendMessage(plugin.getMessageHandler().getMessage("now-playing").replace("%gamename%", game));
        plugin.getPlayerHandler().loadGame(sender, foundPath);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) return new ArrayList<>(plugin.getRomHandler().getRoms().keySet());
        sender.sendMessage(String.join(" ", args));
        return super.tabComplete(sender, alias, args);
    }
}
