package org.dreaght.killwarrant;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.utils.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Config {
    private final Plugin plugin;
    private final FileConfiguration config;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        fillConfig();
    }

    public void fillConfig() {
        if (!config.contains("min-award")) {
            config.set("min-award", 50);
        }
        if (!config.contains("orders")) {
            config.createSection("orders");
        }
        if (!config.contains("boss-bar-time")) {
            config.set("boss-bar-time", 5);
        }
        plugin.saveConfig();
    }

    public String getMessageByPath(String path) {
        return config.getString(path);
    }

    public List<String> getLinesByPath(String path) {
        return config.getStringList(path);
    }

    public double getBossBarTime() {
        return config.getDouble("boss-bar-time");
    }

    public Set<String> getTargetList() {
        return Objects.requireNonNull(config.getConfigurationSection("orders")).getKeys(false);
    }

    public Order getOrderByTargetName(String targetName) {
        ConfigurationSection section = config.getConfigurationSection("orders." + targetName);

        if (section == null) {
            return null;
        }

        return new Order(section.getName(), section.getString("client"), section.getDouble("award"));
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        getTargetList().forEach(target -> orders.add(getOrderByTargetName(target)));

        return orders;
    }

    public void addTarget(Order order) {
        String targetName = order.getTargetName();

        if (!getTargetList().contains(targetName)) {
            config.createSection("orders." + targetName);
        }
        config.set("orders." + targetName + ".client", order.getClientName());
        config.set("orders." + targetName + ".award", order.getAward());
        config.set("orders." + targetName + ".target-location", order.getTargetLocation());
        plugin.saveConfig();
    }

    public void removeTarget(String targetName) {
        config.set("orders." + targetName, null);
        plugin.saveConfig();
    }

    public double getMinAward() {
        return config.getDouble("min-award");
    }

    public int getLocationUpdatePeriod() {
        return config.getInt("location-update-period");
    }

    public Location getLocation(String targetName) {
        return config.getLocation("orders." + targetName + ".target-location");
    }
}
