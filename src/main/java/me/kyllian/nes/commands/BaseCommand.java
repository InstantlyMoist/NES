package me.kyllian.nes.commands;

import me.kyllian.nes.NESPlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseCommand extends BukkitCommand {

    private static BaseComponent helpComponent = null;

    private final Set<BaseCommand> subCommands = new HashSet<>();
    protected final NESPlugin plugin;

    public BaseCommand(
            @NotNull NESPlugin plugin,
            @NotNull String name,
            @NotNull String description,
            @NotNull String usageMessage,
            @NotNull List<String> aliases,
            @Nullable String permission
    ) {
        super(name, description, usageMessage, aliases);
        setPermission(permission);
        this.plugin = plugin;
    }

    public abstract void handle(@NotNull Player sender, @NotNull String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sendMessageKey(sender, "player-only");
            return false;
        }
        final Player player = (Player) sender;
        if (!testPermission(sender)) {
            sendMessageKey(sender, "no-permission");
            return false;
        }
        if (args.length == 0 && subCommands.isEmpty()) {
            handle(player, args);
            return true;
        }
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        BaseCommand sub = getSubCommand(args[0]);
        if (sub == null) {
            if (subCommands.isEmpty()) {
                handle(player, args);
                return true;
            }
            showHelp(sender);
            return true;
        }
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        sub.execute(sender, commandLabel, newArgs);
        return true;
    }

    protected void sendMessageKey(@NotNull CommandSender sender, @NotNull String key) {
        sender.sendMessage(plugin.getMessageHandler().getMessage(key));
    }

    protected void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    protected BaseCommand getSubCommand(@NotNull String name) {
        return subCommands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void addSubCommand(@NotNull BaseCommand command) {
        subCommands.add(command);
    }

    protected void showHelp(CommandSender sender) {
        if (helpComponent == null) {
            BaseComponent component = new TextComponent(plugin.getMessageHandler().getMessage("playable"));
            plugin.getRomHandler().getRoms().keySet().forEach(rom -> {
                TextComponent romClick = new TextComponent(colorTranslate("\n" + plugin.getMessageHandler().getMessage("gamename-prefix") + rom));
                romClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nes play " + rom));
                try {
                    romClick.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(plugin.getMessageHandler().getMessage("click-to-play").replace("%gamename%", rom))));
                } catch (NoClassDefFoundError ignored) {
                    // Hover not found, just don't add it
                }
                component.addExtra(romClick);
            });
            component.addExtra(plugin.getMessageHandler().getMessage("instructions"));
            helpComponent = component;
        }
        if (plugin.getRomHandler().getRoms().isEmpty()) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("no-games"));
            return;
        }
        sender.spigot().sendMessage(helpComponent);
    }

    protected @NotNull String colorTranslate(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (subCommands.isEmpty()) {
            return super.tabComplete(sender, alias, args);
        }
        if (args.length == 1) {
            List<String> subCommandNames = new ArrayList<>();
            for (BaseCommand subCommand : subCommands) {
                if (subCommand.testPermission(sender)) {
                    subCommandNames.add(subCommand.getName());
                }
            }
            return subCommandNames;
        }
        for (BaseCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                return subCommand.tabComplete(sender, alias, newArgs);
            }
        }
        return super.tabComplete(sender, alias, args);
    }
}
