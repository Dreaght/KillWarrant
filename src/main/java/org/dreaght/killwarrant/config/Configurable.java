package org.dreaght.killwarrant.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public abstract class Configurable {
    protected Plugin plugin;
    protected FileConfiguration config;

    protected Configurable(Plugin plugin, String fileName, String fileExtension) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), fileName + "." + fileExtension);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            plugin.saveResource(fileName + "." + fileExtension, false);
        }
    }
}
