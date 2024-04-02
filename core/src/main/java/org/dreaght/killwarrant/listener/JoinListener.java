package org.dreaght.killwarrant.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.util.Order;
import org.dreaght.killwarrant.manager.OrderManager;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OrderManager orderManager = OrderManager.getInstance();
        Player player = event.getPlayer();
        ConfigManager configManager = ConfigManager.getInstance();

        Order order = configManager.getOrdersConfig().getOrderByTargetName(player.getName());
        if (order == null) {
            return;
        }

        orderManager.saveOrder(order);
    }
}
