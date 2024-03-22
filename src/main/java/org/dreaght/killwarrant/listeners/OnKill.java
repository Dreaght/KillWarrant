package org.dreaght.killwarrant.listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.utils.Order;

import java.util.Objects;

public class OnKill implements Listener {
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player deathPlayer = event.getEntity();
        String targetName = deathPlayer.getName();

        if (Objects.requireNonNull(deathPlayer.getLastDamageCause()).getEntity() instanceof Player) {

            Player killer = event.getEntity().getKiller();

            Config config = new Config();

            if (config.getTargetList().contains(targetName)) {
                Order order = config.getOrderByTargetName(targetName);

                Bukkit.broadcastMessage(String.format("§cKillWarrant §7>> §fPlayer §a%s §fhas killed §c%s §fand got §6%d",
                        killer.getName(), targetName, order.getAward()));
                Bukkit.getOnlinePlayers().forEach(player -> deathPlayer.getWorld().playSound(player.getLocation(),
                        Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3.0F, 0.5F));

                config.removeTarget(targetName);

                Economy economy = KillWarrant.getEcon();
                economy.depositPlayer(killer, order.getAward());
            }
        }
    }
}
