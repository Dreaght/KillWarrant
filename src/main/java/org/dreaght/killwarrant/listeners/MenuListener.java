package org.dreaght.killwarrant.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.managers.MenuManager;

public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        ConfigManager configManager = ConfigManager.getInstance();

        if (!(event.getView().getTitle().equalsIgnoreCase(configManager.getMessageConfig().getMessageByPath("messages.menu.title")))) {
            return;
        }

        event.setCancelled(true);
    }

}
