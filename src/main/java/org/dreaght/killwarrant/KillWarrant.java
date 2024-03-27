package org.dreaght.killwarrant;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dreaght.killwarrant.commands.KillerCommand;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.config.MessageConfig;
import org.dreaght.killwarrant.config.OrdersConfig;
import org.dreaght.killwarrant.config.SettingsConfig;
import org.dreaght.killwarrant.listeners.JoinListener;
import org.dreaght.killwarrant.listeners.MenuListener;
import org.dreaght.killwarrant.listeners.KillListener;
import org.dreaght.killwarrant.listeners.QuitListener;
import org.dreaght.killwarrant.utils.OrderManager;

import java.util.Objects;

public final class KillWarrant extends JavaPlugin {
    private static Economy econ = null;
    private static OrderManager orderManager;

    public static Economy getEcon() {
        return econ;
    }

    public static OrderManager getOrderManager() {
        return orderManager;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigManager.init(this);

        getServer().getPluginManager().registerEvents(new KillListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        Objects.requireNonNull(getCommand("killer")).setExecutor(new KillerCommand(this));

        orderManager = new OrderManager(this);
        orderManager.loadOrders();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
