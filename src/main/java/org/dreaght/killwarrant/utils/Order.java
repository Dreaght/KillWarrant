package org.dreaght.killwarrant.utils;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Order {
    private String targetName;
    private String clientName;

    private double award;

    private Location targetLocation;

    private BukkitRunnable runnable;

    public Order(String targetName, String clientName, double award) {
        this.targetName = targetName;
        this.clientName = clientName;
        this.award = award;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String newTargetName) {
        this.targetName = newTargetName;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String newClientName) {
        this.clientName = newClientName;
    }

    public double getAward() {
        return this.award;
    }

    public void setAward(Integer award) {
        this.award = award;
    }

    public Location getTargetLocation() {
        return this.targetLocation;
    }

    public void setTargetLocation(Location location) {
        this.targetLocation = location;
    }

    public BukkitRunnable getRunnable() {
        return this.runnable;
    }

    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }
}
