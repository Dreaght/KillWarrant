package org.dreaght.killwarrant.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dreaght.killwarrant.util.Order;
import org.dreaght.killwarrant.manager.OrderManager;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        OrderManager orderManager = OrderManager.getInstance();
        Player player = event.getPlayer();

        Order order = orderManager.getOrderByTargetName(player.getName());
        if (order == null) {
            return;
        }

        orderManager.removeOrder(order);
    }
}
