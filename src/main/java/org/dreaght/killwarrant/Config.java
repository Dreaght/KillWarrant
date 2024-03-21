package org.dreaght.killwarrant;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.utils.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Config {
    private final Plugin plugin = KillWarrant.getInstance();
    private final FileConfiguration config = plugin.getConfig();

    public Config() {
        fillConfig();
    }

    public void fillConfig() {
        if (!config.contains("min-award")) {
            config.set("min-award", 50);
        }
        if (!config.contains("orders")) {
            config.createSection("orders");
        }
        plugin.saveConfig();
    }

    public Set<String> getTargetList() {
        return Objects.requireNonNull(config.getConfigurationSection("orders")).getKeys(false);
    }

    public Order getOrderByTargetName(String targetName) {
        ConfigurationSection section = config.getConfigurationSection("orders." + targetName);
        assert section != null;
        return new Order(section.getName(), section.getString("client"), section.getInt("award"));
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
        plugin.saveConfig();
    }

    public void removeTarget(String targetName) {
        config.set("orders." + targetName, null);
        plugin.saveConfig();
    }

    public Integer getMinAward() {
        return config.getInt("min-award");
    }

}
