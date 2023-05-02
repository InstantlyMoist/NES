package me.kyllian.nes.listeners;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.listeners.manager.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DisconnectListener extends PacketListener {

    public DisconnectListener(NESPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleReceive(PacketReceiveEvent event) {
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getUUID());
        if (player == null) return;
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (!pocket.isEmpty()) pocket.stopEmulator(player);
        plugin.getPlayerHandler().removePocket(player);
    }

}

