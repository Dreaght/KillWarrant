package org.dreaght.killwarrant.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.OrderManager;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OrderManager orderManager = KillWarrant.getOrderManager();
        Player player = event.getPlayer();
        Config config = KillWarrant.getCfg();

        Order order = config.getOrderByTargetName(player.getName());
        if (order == null) {
            return;
        }

        orderManager.saveOrder(order);
    }
}
