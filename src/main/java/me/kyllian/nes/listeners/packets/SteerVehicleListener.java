package me.kyllian.nes.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import org.bukkit.entity.Player;

public class SteerVehicleListener {

    public SteerVehicleListener(NESPlugin NESPlugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(NESPlugin, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                Pocket pocket = NESPlugin.getPlayerHandler().getPocket(player);
                if (pocket.isEmpty()) return;
                PacketContainer container = event.getPacket();
                float sideways = container.getFloat().read(0);
                float forward = container.getFloat().read(1);
                pocket.getButtonToggleHelper().press(PuppetController.Button.LEFT, sideways > 0);
                pocket.getButtonToggleHelper().press(PuppetController.Button.RIGHT, sideways < 0);
                pocket.getButtonToggleHelper().press(PuppetController.Button.UP, forward > 0);
                pocket.getButtonToggleHelper().press(PuppetController.Button.DOWN, forward < 0);
                pocket.getButtonToggleHelper().press(PuppetController.Button.A, container.getBooleans().read(0));
                if (container.getBooleans().read(1)) {
                    pocket.stopEmulator(player);
                    player.sendMessage(NESPlugin.getMessageHandler().getMessage("stopped"));
                    return;
                }
            }
        });
    }
}
