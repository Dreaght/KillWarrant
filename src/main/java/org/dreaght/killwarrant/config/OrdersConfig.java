package org.dreaght.killwarrant.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.utils.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrdersConfig extends Configurable {
    protected OrdersConfig(Plugin plugin, String fileName, String fileExtension) {
        super(plugin, fileName, fileExtension);
    }

    public Set<String> getTargetList() {
        return Objects.requireNonNull(config.getConfigurationSection("orders")).getKeys(false);
    }

    public Order getOrderByTargetName(String targetName) {
        ConfigurationSection section = config.getConfigurationSection("orders." + targetName);

        if (section == null) {
            return null;
        }

        String dateString = section.getString("date");

        return new Order(
                Bukkit.getPlayer(section.getName()),
                Bukkit.getPlayer(Objects.requireNonNull(section.getString("client"))),
                section.getDouble("award"),
                LocalDateTime.parse(dateString));
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
        config.set("orders." + targetName + ".date", order.getDate().toString());
        plugin.saveConfig();
    }

    public void removeTarget(String targetName) {
        config.set("orders." + targetName, null);
        plugin.saveConfig();
    }
}
