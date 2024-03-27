package org.dreaght.killwarrant.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.gui.MenuManager;
import org.dreaght.killwarrant.utils.EcoTransactions;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.OrderManager;
import org.dreaght.killwarrant.utils.ParseValue;

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
            OrderManager orderManager = KillWarrant.getOrderManager();
            Order order = orderManager.getOrderByTargetName(targetName);

            if (order.getClientName().equals(killer.getName())) {
                killer.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.got-nothing"));
                return;
            }

            LocalDateTime date = order.getDate();
            LocalDateTime currentDate = LocalDateTime.now();

            double finalAward = EcoTransactions.calculateFinalAward(date, currentDate, order.getAward());
            double survivedTime = EcoTransactions.calculateMinutesDifference(date, currentDate);

            Bukkit.broadcastMessage(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killed"),
                    new String[]{"KILLER_NAME", "TARGET_NAME", "AWARD", "TIME"},
                    new Object[]{killer.getName(), targetName, finalAward, survivedTime}));
            Bukkit.getOnlinePlayers().forEach(player -> deathPlayer.getWorld().playSound(player.getLocation(),
                    Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3.0F, 0.5F));

            EcoTransactions.depositPlayer(killer, finalAward);

            if (survivedTime >= configManager.getSettingsConfig().getMinOrderTime()) {
                EcoTransactions.depositPlayer(deathPlayer, order.getAward() - finalAward);
                deathPlayer.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.survive-time-award"));
            }

            configManager.getOrdersConfig().removeTarget(targetName);

            orderManager.removeOrder(order);
            MenuManager.updateLocForAllMenu(0, 0);
        }
    }


}
