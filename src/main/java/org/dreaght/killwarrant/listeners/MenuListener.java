package org.dreaght.killwarrant.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.dreaght.killwarrant.Config;

public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Config config = new Config();

        if (!(event.getView().getTitle().equalsIgnoreCase(config.getMessageByPath("messages.menu.title")))) {
            return;
        }

        event.setCancelled(true);
    }

}
