package org.dreaght.killwarrant.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.gui.MenuManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderManager {
    private final Plugin plugin;

    private final Set<Order> orders = new HashSet<>();

    public OrderManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadOrders() {
        List<Order> orders = KillWarrant.getCfg().getAllOrders();

        for (Order order : orders) {
            this.saveOrder(order);
        }
    }

    public void saveOrder(Order order) {
        Config config = KillWarrant.getCfg();
        int seconds = config.getLocationUpdatePeriod();

        AtomicInteger count = new AtomicInteger(seconds);

        Player target = Bukkit.getPlayer(order.getTargetName());

        if (target == null) {
            return;
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            Location lastLocation = target.getLocation();

            @Override
            public void run() {
                Player target = Bukkit.getPlayer(order.getTargetName());

                if (target == null || !target.isOnline()) {
                    order.setRunnable(null);
                    removeOrder(order);
                    cancel();
                    return;
                }

                if (count.get() == 1) {
                    count.set(seconds);
                    order.setTargetLocation(target.getLocation());
                    lastLocation = target.getLocation();
                } else {
                    order.setTargetLocation(lastLocation);
                }

                MenuManager.updateLocForAllMenu(count.decrementAndGet());

                if (!orders.contains(order)) {
                    cancel();
                }
            }
        };

        runnable.runTaskTimer(plugin, 0, 20);
        order.setRunnable(runnable);
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        order.getRunnable().cancel();
        order.setRunnable(null);
        orders.remove(order);
    }

    public Order getOrderByTargetName(String targetName) {
        for (Order order : orders) {
            if (order.getTargetName().equals(targetName)) {
                return order;
            }
        }

        return null;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }
}
