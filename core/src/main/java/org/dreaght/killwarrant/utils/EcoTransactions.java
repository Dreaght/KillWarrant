package org.dreaght.killwarrant.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.config.ConfigManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class EcoTransactions {
    public static void depositPlayer(Player player, double award) {
        Economy economy = KillWarrant.getEcon();
        economy.depositPlayer(player, award);
    }

    public static void refundMoney(Order order) {
        ConfigManager configManager = ConfigManager.getInstance();

        depositPlayer(order.getClient(), order.getAward());
        Bukkit.broadcastMessage(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.refund-money"),
                new String[]{"TARGET_NAME"},
                new Object[]{order.getTargetName()}));
        configManager.getOrdersConfig().removeTarget(order.getTargetName());
    }

    public static double calculateFinalAward(LocalDateTime dateTime1, LocalDateTime dateTime2, double award, ConfigManager configManager) {
        long minutesDifference = calculateMinutesDifference(dateTime1, dateTime2);
        double ratio = (double) minutesDifference / configManager.getSettingsConfig().getMaxOrderTime();
        return award - (award * ratio);
    }

    public static long calculateMinutesDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        Duration duration = Duration.between(dateTime1, dateTime2);
        return Math.abs(duration.toMinutes());
    }
}
