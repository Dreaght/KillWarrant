package org.dreaght.killwarrant.config;

import org.bukkit.plugin.Plugin;

public class SettingsConfig extends Configurable {
    protected SettingsConfig(Plugin plugin, String fileName, String fileExtension) {
        super(plugin, fileName, fileExtension);
    }

    public double getBossBarTime() {
        return config.getDouble("boss-bar-time");
    }

    public double getMinAward() {
        return config.getDouble("min-award");
    }

    public int getLocationUpdatePeriod() {
        return config.getInt("location-update-period");
    }

    public long getMaxOrderTime() {
        return config.getLong("max-order-time");
    }

    public long getMinOrderTime() {
        return config.getLong("min-order-time");
    }

    public boolean getCanOrderYourself() {
        return config.getBoolean("can-order-yourself");
    }

    public String getDecimalAwardFormat() {
        return config.getString("decimal-award-format");
    }

    public String getDecimalLocationFormat() {
        return config.getString("decimal-location-format");
    }
}
