package me.kyllian.nes.listeners;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.helpers.ButtonToggleHelper;
import me.kyllian.nes.listeners.manager.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SteerVehicleListener extends PacketListener {

    public SteerVehicleListener(NESPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event);
            Player player = Bukkit.getPlayer(event.getUser().getUUID());
            if (player == null) return;
            Pocket pocket = plugin.getPlayerHandler().getPocket(player);
            if (pocket.isEmpty()) return;

            float forward = wrapper.getForward();
            float sideways = wrapper.getSideways();

            ButtonToggleHelper bh = pocket.getButtonToggleHelper();
            bh.press(PuppetController.Button.LEFT, sideways > 0);
            bh.press(PuppetController.Button.RIGHT, sideways < 0);
            bh.press(PuppetController.Button.UP, forward > 0);
            bh.press(PuppetController.Button.DOWN, forward < 0);
            bh.press(PuppetController.Button.A, wrapper.isJump());
            if (wrapper.isUnmount()) {
                pocket.stopEmulator(player);
                player.sendMessage(plugin.getMessageHandler().getMessage("stopped"));
            }
        }
    }

}

