package org.dreaght.killwarrant.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.nms.NmsStuff;
import org.dreaght.killwarrant.util.EcoTransactions;
import org.dreaght.killwarrant.util.Order;
import org.dreaght.killwarrant.manager.OrderManager;
import org.dreaght.killwarrant.util.ParseValue;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public class KillListener implements Listener {
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player deathPlayer = event.getEntity();
        String targetName = deathPlayer.getName();

        Objects.requireNonNull(event.getEntity());
        Player killer = event.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.getOrdersConfig().getTargetList().contains(targetName)) {
            OrderManager orderManager = OrderManager.getInstance();
            Order order = orderManager.getOrderByTargetName(targetName);

            if (order.getClientName().equals(killer.getName())) {
                killer.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.got-nothing"));
                return;
            }

            LocalDateTime date = order.getDate();
            LocalDateTime currentDate = LocalDateTime.now();

            double award = order.getAward();

            if (configManager.getSettingsConfig().getAwardDecrease()) {
                award = EcoTransactions.calculateFinalAward(date, currentDate, order.getAward(), configManager);
            }

            double survivedTime = EcoTransactions.calculateMinutesDifference(date, currentDate);

            DecimalFormat decimalFormat = new DecimalFormat(configManager.getSettingsConfig().getDecimalAwardFormat());

            Bukkit.broadcastMessage(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killed"),
                    new String[]{"KILLER_NAME", "TARGET_NAME", "AWARD", "TIME"},
                    new Object[]{killer.getName(), targetName, decimalFormat.format(award), survivedTime}));
            Bukkit.getOnlinePlayers().forEach(player -> deathPlayer.getWorld().playSound(player.getLocation(),
                    NmsStuff.getFireballExplode(), 3.0F, 0.5F));

            EcoTransactions.depositPlayer(killer, award);

            configManager.getOrdersConfig().removeTarget(targetName);

            orderManager.removeOrder(order);
        }
    }


}
