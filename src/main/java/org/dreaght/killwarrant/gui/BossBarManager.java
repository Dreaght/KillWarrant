package org.dreaght.killwarrant.gui;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.utils.Order;

public class BossBarManager {
    private final Plugin plugin;

    public BossBarManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void makeBossBar(World world, Order order) {
        Config config = new Config();

        Server server = plugin.getServer();
        final BukkitTask[] finalProgressTask = new BukkitTask[1];

        BukkitTask task = server.getScheduler().runTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> world.playSound(player.getLocation(),
                    Sound.ENTITY_ENDER_DRAGON_HURT, 3.0F, 0.5F));
            BossBar bar = Bukkit.getServer().createBossBar(
                    String.format(config.getMessageByPath("messages.boss-bar.order-announcement"),
                            order.getTargetName(), order.getAward()),
                    BarColor.GREEN, BarStyle.SOLID);
            bar.setVisible(true);
            bar.setProgress(1);

            plugin.getServer().getOnlinePlayers().forEach(bar::addPlayer);

            final double[] progress = {1.0};

            finalProgressTask[0] = server.getScheduler().runTaskTimer(plugin, () -> {
                progress[0] -= 0.05 / config.getBossBarTime();
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
