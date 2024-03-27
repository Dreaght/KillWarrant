package org.dreaght.killwarrant.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.gui.MenuManager;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.OrderManager;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        OrderManager orderManager = KillWarrant.getOrderManager();
        Player player = event.getPlayer();

        Order order = orderManager.getOrderByTargetName(player.getName());
        if (order == null) {
            return;
        }

        orderManager.removeOrder(order);
        MenuManager.updateLocForAllMenu(0, 0);
    }
}
