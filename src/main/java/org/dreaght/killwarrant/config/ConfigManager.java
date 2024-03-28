package org.dreaght.killwarrant.config;

import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private static ConfigManager instance;
    private final SettingsConfig settingsConfig;
    private final OrdersConfig ordersConfig;
    private final MessageConfig messageConfig;

    private ConfigManager(Plugin plugin) {
        this.settingsConfig = new SettingsConfig(plugin, "config", "yml");
        this.ordersConfig = new OrdersConfig(plugin, "orders", "yml");
        this.messageConfig = new MessageConfig(plugin, "messages", "yml");
    }

    public static void init(Plugin plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public OrdersConfig getOrdersConfig() {
        return ordersConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
}
