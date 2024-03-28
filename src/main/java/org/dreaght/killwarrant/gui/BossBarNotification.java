package org.dreaght.killwarrant.gui;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.ParseValue;

import java.text.DecimalFormat;

public class BossBarNotification {
    private final Plugin plugin;

    public BossBarNotification(Plugin plugin) {
        this.plugin = plugin;
    }

    public void makeBossBar(World world, Order order) {
        ConfigManager configManager = ConfigManager.getInstance();

        DecimalFormat decimalFormat = new DecimalFormat(configManager.getSettingsConfig().getDecimalAwardFormat());

        Server server = plugin.getServer();
        final BukkitTask[] finalProgressTask = new BukkitTask[1];

        server.getScheduler().runTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> world.playSound(player.getLocation(),
                    Sound.ENTITY_ENDER_DRAGON_HURT, 3.0F, 0.5F));
            BossBar bar = Bukkit.getServer().createBossBar(
                    ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.boss-bar.order-announcement"),
                            new String[]{"TARGET_NAME", "AWARD"},
                            new Object[]{order.getTargetName(), decimalFormat.format(order.getAward())}),
                    BarColor.GREEN, BarStyle.SOLID);
            bar.setVisible(true);
            bar.setProgress(1);

            plugin.getServer().getOnlinePlayers().forEach(bar::addPlayer);

            final double[] progress = {1.0};

            finalProgressTask[0] = server.getScheduler().runTaskTimer(plugin, () -> {
                progress[0] -= 0.05 / configManager.getSettingsConfig().getBossBarTime();
                if (progress[0] <= 0) {
                    bar.removeAll();
                    finalProgressTask[0].cancel();
                } else {
                    bar.setProgress(progress[0]);
                }
            }, 1L, 1L);
        });
    }
}
