package me.kyllian.nes.listeners.packets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerBoat;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.grapeshot.halfnes.ui.PuppetController;
import me.kyllian.nes.NESPlugin;
import me.kyllian.nes.data.Pocket;
import me.kyllian.nes.helpers.ButtonToggleHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SteerVehicleListener extends PacketListenerAbstract {

    private final NESPlugin plugin;
    public SteerVehicleListener(NESPlugin plugin) {
        this.plugin = plugin;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();


    // TODO The reason these are seperated is for future, to replace all bukkit listeners with packet alternatives
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type == PacketType.Play.Client.STEER_VEHICLE) {
            PacketReceiveEvent copy = event.clone();
            executor.execute(() -> {
                handleSteerVehicle(copy);
                copy.cleanUp();
            });
        }
    }

    private void handleSteerVehicle(PacketReceiveEvent event) {
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
