package org.dreaght.killwarrant.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.gui.MenuManager;

import java.time.Duration;
import java.time.LocalDateTime;
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
        ConfigManager configManager = ConfigManager.getInstance();

        List<Order> orders = configManager.getOrdersConfig().getAllOrders();

        for (Order order : orders) {
            this.saveOrder(order);
        }
    }

    public void saveOrder(Order order) {
        ConfigManager configManager = ConfigManager.getInstance();

        int seconds = configManager.getSettingsConfig().getLocationUpdatePeriod();

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

                LocalDateTime date = order.getDate();
                LocalDateTime currentDate = LocalDateTime.now();

                MenuManager.updateLocForAllMenu(count.decrementAndGet(), timeLeft(date, currentDate));

                if (outOfTime(date, currentDate)) {
                    EcoTransactions.refundMoney(order);
                    removeOrder(order);
                    cancel();
                }

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
        ConfigManager configManager = ConfigManager.getInstance();

        order.getRunnable().cancel();
        order.setRunnable(null);
        orders.remove(order);
        configManager.getOrdersConfig().removeTarget(order.getTargetName());
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

    private boolean outOfTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        ConfigManager configManager = ConfigManager.getInstance();

        Duration duration = Duration.between(dateTime1, dateTime2);
        long minutesDifference = Math.abs(duration.toMinutes());

        return minutesDifference >= configManager.getSettingsConfig().getMaxOrderTime();
    }

    private long timeLeft(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        ConfigManager configManager = ConfigManager.getInstance();

        Duration duration = Duration.between(dateTime1, dateTime2);
        long minutesDifference = Math.abs(duration.toMinutes());

        return configManager.getSettingsConfig().getMaxOrderTime() - minutesDifference;
    }
}
