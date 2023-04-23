package me.kyllian.nes.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class RideableArmorstand {

    private final Player player;
    private final JavaPlugin plugin;

    private final ArmorStand armorStand;


    public RideableArmorstand(Player player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
        this.armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        this.armorStand.setInvulnerable(true);
        this.armorStand.setGravity(false);
        this.armorStand.setSilent(true);
        this.armorStand.setSmall(true);
        this.armorStand.setMarker(true);
        this.armorStand.setCanPickupItems(false);
        this.armorStand.setVisible(false);
        this.armorStand.addPassenger(player);
    }

    public void remove() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            armorStand.removePassenger(player);
            armorStand.remove();
        });
    }

}
