package org.dreaght.killwarrant.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.utils.EcoTransactions;
import org.dreaght.killwarrant.utils.Order;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderManager {
    private static OrderManager instance;
    private final Plugin plugin;

    private final Set<Order> orders = new HashSet<>();

    private OrderManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public static OrderManager init(Plugin plugin) {
        if (instance == null) {
            instance = new OrderManager(plugin);
        }

        return instance;
    }

    public static OrderManager getInstance() {
        return instance;
    }

    public void loadOrders() {
        ConfigManager configManager = ConfigManager.getInstance();

        List<Order> orders = configManager.getOrdersConfig().getAllOrders();

        if (orders == null) {
            return;
        }

        for (Order order : orders) {
            this.saveOrder(order);
        }
    }

    public void saveOrder(Order order) {
        ConfigManager configManager = ConfigManager.getInstance();
        int seconds = configManager.getSettingsConfig().getLocationUpdatePeriod();

        Player target = order.getTarget();

        if (target == null) {
            return;
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            Location lastLocation = target.getLocation();

            @Override
            public void run() {
                if (!target.isOnline() || !orders.contains(order)) {
                    removeOrder(order);
                    return;
                }

                LocalDateTime date = order.getDate();
                LocalDateTime currentDate = LocalDateTime.now();

                long differenceInSeconds = java.time.Duration.between(date, currentDate).getSeconds();
                long remainder = seconds - (differenceInSeconds % seconds) - 1;

                if (remainder == 0) {
                    order.setTargetLocation(target.getLocation());
                    lastLocation = target.getLocation();
                } else {
                    order.setTargetLocation(lastLocation);
                }

                if (outOfTime(date, currentDate)) {
                    EcoTransactions.refundMoney(order);
                    removeOrder(order);
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

    public long timeLeft(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        ConfigManager configManager = ConfigManager.getInstance();

        Duration duration = Duration.between(dateTime1, dateTime2);
        long minutesDifference = Math.abs(duration.toMinutes());

        return configManager.getSettingsConfig().getMaxOrderTime() - minutesDifference;
    }

    public boolean outOfTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        ConfigManager configManager = ConfigManager.getInstance();

        Duration duration = Duration.between(dateTime1, dateTime2);
        long minutesDifference = Math.abs(duration.toMinutes());

        return minutesDifference >= configManager.getSettingsConfig().getMaxOrderTime();
    }
}
