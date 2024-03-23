package org.dreaght.killwarrant.listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.gui.MenuManager;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.OrderManager;
import org.dreaght.killwarrant.utils.ParseValue;

import java.util.Objects;

public class KillListener implements Listener {
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player deathPlayer = event.getEntity();
        String targetName = deathPlayer.getName();

        if (Objects.requireNonNull(deathPlayer.getLastDamageCause()).getEntity() instanceof Player) {

            Player killer = event.getEntity().getKiller();

            Config config = KillWarrant.getCfg();

            if (config.getTargetList().contains(targetName)) {
                OrderManager orderManager = KillWarrant.getOrderManager();
                Order order = orderManager.getOrderByTargetName(targetName);

                Bukkit.broadcastMessage(ParseValue.parseWithBraces(config.getMessageByPath("messages.killed"),
                        new String[]{"KILLER_NAME", "TARGET_NAME", "AWARD"},
                        new Object[]{killer.getName(), targetName, order.getAward()}));
                Bukkit.getOnlinePlayers().forEach(player -> deathPlayer.getWorld().playSound(player.getLocation(),
                        Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3.0F, 0.5F));

                config.removeTarget(targetName);

                Economy economy = KillWarrant.getEcon();
                economy.depositPlayer(killer, order.getAward());

                orderManager.removeOrder(order);
                MenuManager.updateLocForAllMenu(0);
            }
        }
    }
}
