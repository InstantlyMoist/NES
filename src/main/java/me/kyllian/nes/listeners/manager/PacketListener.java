package me.kyllian.nes.listeners.manager;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import me.kyllian.nes.NESPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class PacketListener extends PacketListenerAbstract {

   // protected static final Executor EXECUTOR = Executors.newCachedThreadPool();

    protected final NESPlugin plugin;

    public PacketListener(NESPlugin plugin) {
        this.plugin = plugin;
    }


    public abstract void handleReceive(PacketReceiveEvent event);
    public void handleSend(PacketSendEvent event) {}


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        handleReceive(event);
//        PacketReceiveEvent clone = event.clone();
//        EXECUTOR.execute(() -> {
//            handleReceive(clone);
//            clone.cleanUp();
//        });
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        handleSend(event);
//        PacketSendEvent clone = event.clone();
//        EXECUTOR.execute(() -> {
//            handleSend(clone);
//            clone.cleanUp();
//        });
    }
}
