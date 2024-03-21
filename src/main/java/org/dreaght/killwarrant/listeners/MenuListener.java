package org.dreaght.killwarrant.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Kill Warrants"))) {
            return;
        }

        event.setCancelled(true);
    }

}
