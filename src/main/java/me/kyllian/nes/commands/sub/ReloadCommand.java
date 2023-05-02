package me.kyllian.nes.commands.sub;

import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.commands.BaseCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand(@NotNull NESPlugin plugin) {
        super(
                plugin,
                "reload",
                "Reload the NES plugin",
                "/nes reload",
                Collections.emptyList(),
                "nes.reload"
                );
    }


    @Override
    public void handle(@NotNull Player sender, @NotNull String[] args) {
        try {
            plugin.getRomHandler().loadRoms();
            sender.sendMessage(colorTranslate("&AReloading complete!"));
        } catch (IOException exception) {
            sender.sendMessage(colorTranslate("&cReloading failed!"));
        }
    }
}
