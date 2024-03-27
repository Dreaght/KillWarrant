package org.dreaght.killwarrant.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class Order {
    private Player target;
    private Player client;

    private double award;

    private Location targetLocation;

    private BukkitRunnable runnable;

    private LocalDateTime date;

    public Order(Player targetName, Player clientName, double award, LocalDateTime date) {
        this.target = targetName;
        this.client = clientName;
        this.award = award;
        this.date = date;
    }

    public String getTargetName() {
        return target.getName();
    }

    public String getClientName() {
        return client.getName();
    }

    public Player getTarget() {
        return target;
    }

    public Player getClient() {
        return client;
    }

    public double getAward() {
        return award;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location location) {
        targetLocation = location;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
