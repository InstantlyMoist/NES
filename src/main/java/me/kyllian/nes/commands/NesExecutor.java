package me.kyllian.nes.commands;

import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.IOException;

public class NesExecutor implements CommandExecutor {

    private NESPlugin plugin;

    public NesExecutor(NESPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("player-only"));
            return true;
        }
        Player player = (Player) sender;
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                if (pocket.isEmpty()) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("no-game"));
                    return true;
                }
                pocket.stopEmulator(player);
                player.sendMessage(plugin.getMessageHandler().getMessage("stopped"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    plugin.getRomHandler().loadRoms();
                    sender.sendMessage(colorTranslate("&AReloading complete!"));
                } catch (IOException exception) {
                    sender.sendMessage(colorTranslate("&cReloading failed!"));
                }
                return true;
            }
        }
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("play")) {
                if (!pocket.isEmpty()) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("already-running"));
                    return true;
                }
                if (!player.hasPermission("nes.play")) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("no-permission"));
                    return true;
                }
                String gameName = "";
                for (int i = 1; i != args.length; i++) {
                    gameName += args[i] + " ";
                }
                gameName = gameName.trim();
                String foundPath = plugin.getRomHandler().getRoms().get(gameName);
                if (foundPath == null) {
                    sender.sendMessage(plugin.getMessageHandler().getMessage("not-found"));
                    showHelp(sender);
                    return true;
                }
                if (plugin.isProtocolLib()) {
                    Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);
                    entity.addPassenger(player);
                    entity.setSilent(true);
                    entity.setInvulnerable(true);
                    entity.setGravity(false);

                    pocket.setArrow(entity);
                }
                player.sendMessage(plugin.getMessageHandler().getMessage(plugin.isProtocolLib() ? "now-playing-protocollib" : "now-playing-normal").replace("%gamename%", gameName));
                plugin.getPlayerHandler().loadGame(player, foundPath);
                return true;
            }
//            if (args[0].equalsIgnoreCase("join")) {
//                if (!pocket.isEmpty()) {
//                    player.sendMessage(plugin.getMessageHandler().getMessage("already-running"));
//                    return true;
//                }
//                if (!player.hasPermission("nes.play")) {
//                    player.sendMessage(plugin.getMessageHandler().getMessage("no-permission"));
//                    return true;
//                }
//                Player target = Bukkit.getPlayer(args[1]);
//                if (target == null) {
//                    player.sendMessage(plugin.getMessageHandler().getMessage("not-found"));
//                    return true;
//                }
//                Pocket targetPocket = plugin.getPlayerHandler().getPocket(target);
//                if (targetPocket.isEmpty()) {
//                    player.sendMessage(plugin.getMessageHandler().getMessage("not-found"));
//                    return true;
//                }
//                if (plugin.isProtocolLib()) {
//                    Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);
//                    entity.addPassenger(player);
//                    entity.setSilent(true);
//                    entity.setInvulnerable(true);
//                    entity.setGravity(false);
//
//                    targetPocket.setCompanionArrow(entity);
//                }
//                player.sendMessage("Now playing with " + target.getName());
//            }
        }
        showHelp(sender);
        return true;
    }

    public void showHelp(CommandSender sender) {
        BaseComponent component = new TextComponent(plugin.getMessageHandler().getMessage("playable"));
        if (plugin.getRomHandler().getRoms().isEmpty()) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("no-games"));
            return;
        }
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
        sender.spigot().sendMessage(component);
    }

    public String colorTranslate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
