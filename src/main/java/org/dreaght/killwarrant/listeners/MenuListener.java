package org.dreaght.killwarrant.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.gui.MenuManager;

public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Config config = KillWarrant.getCfg();

        if (!(event.getView().getTitle().equalsIgnoreCase(config.getMessageByPath("messages.menu.title")))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (MenuManager.contains(player)) {
            MenuManager.removePlayer(player);
        }
    }

}
