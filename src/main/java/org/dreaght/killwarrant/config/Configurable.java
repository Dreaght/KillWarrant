package org.dreaght.killwarrant.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public abstract class Configurable {
    protected Plugin plugin;
    protected FileConfiguration config;
    protected File configFile;

    protected Configurable(@NotNull Plugin plugin, String fileName, String fileExtension) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), fileName + "." + fileExtension);

        if (plugin.getResource(fileName + "." + fileExtension) != null) {
            plugin.saveResource(fileName + "." + fileExtension, false);
        } else {
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
