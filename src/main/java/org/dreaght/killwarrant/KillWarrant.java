package org.dreaght.killwarrant;

import org.bukkit.plugin.java.JavaPlugin;
import org.dreaght.killwarrant.commands.KillerCommand;
import org.dreaght.killwarrant.listeners.MenuListener;

import java.util.Objects;

public final class KillWarrant extends JavaPlugin {
    private static KillWarrant instance;

    public static KillWarrant getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        Objects.requireNonNull(getCommand("killer")).setExecutor(new KillerCommand(this));
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
