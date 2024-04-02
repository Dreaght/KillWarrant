package org.dreaght.killwarrant;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dreaght.killwarrant.command.KillerCommand;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.listener.JoinListener;
import org.dreaght.killwarrant.listener.MenuListener;
import org.dreaght.killwarrant.listener.KillListener;
import org.dreaght.killwarrant.listener.QuitListener;
import org.dreaght.killwarrant.manager.InventoryStateHandler;
import org.dreaght.killwarrant.manager.MenuManager;
import org.dreaght.killwarrant.manager.OrderManager;

import java.util.Objects;

public final class KillWarrant extends JavaPlugin {
    private static Economy econ = null;

    public static Economy getEcon() {
        return econ;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigManager.init(this);
        OrderManager.init(this).loadOrders();
        MenuManager.init(this);

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(this)) {
            return;
        }

        getServer().getPluginManager().registerEvents(new KillListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        Objects.requireNonNull(getCommand("killer")).setExecutor(new KillerCommand(this));
    }

    @Override
    public void onDisable() {
        try {
            InventoryStateHandler.saveInventory(MenuManager.getInstance().getInventory(), this);
        } catch (NullPointerException ignored) {
        }

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
