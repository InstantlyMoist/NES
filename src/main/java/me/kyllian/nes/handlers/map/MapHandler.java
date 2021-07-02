package me.kyllian.nes.handlers.map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MapHandler {

    void loadData();
    void sendMap(Player player);
    void resetMap(ItemStack map);

}
