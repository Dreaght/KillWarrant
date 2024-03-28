package org.dreaght.killwarrant.config;

import org.bukkit.plugin.Plugin;

import java.util.List;

public class MessageConfig extends Configurable {
    protected MessageConfig(Plugin plugin, String fileName, String fileExtension) {
        super(plugin, fileName, fileExtension);
    }

    public String getMessageByPath(String path) {
        return config.getString(path);
    }

    public List<String> getLinesByPath(String path) {
        return config.getStringList(path);
    }
}
