package org.dreaght.killwarrant;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dreaght.killwarrant.commands.KillerCommand;
import org.dreaght.killwarrant.listeners.MenuListener;
import org.dreaght.killwarrant.listeners.OnKill;

import java.util.Objects;

public final class KillWarrant extends JavaPlugin {
    private static KillWarrant instance;
    private static Economy econ = null;

    public static KillWarrant getInstance() {
        return instance;
    }

    public static Economy getEcon() {
        return econ;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new OnKill(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        Objects.requireNonNull(getCommand("killer")).setExecutor(new KillerCommand(this));

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        this.saveDefaultConfig();


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
