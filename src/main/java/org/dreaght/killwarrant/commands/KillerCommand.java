package org.dreaght.killwarrant.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.gui.BossBarNotification;
import org.dreaght.killwarrant.gui.KillerMenu;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.OrderManager;
import org.dreaght.killwarrant.utils.ParseValue;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

public class KillerCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public KillerCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if (player == null) {
            return false;
        }

        return handleArguments(player, args);
    }

    private boolean handleArguments(Player player, String[] args) {
        ConfigManager configManager = ConfigManager.getInstance();

        if (args.length == 0) {
            KillerMenu.handleMenuCreation(player);
        } else if (args.length == 2) {
            DecimalFormat decimalFormat = new DecimalFormat(configManager.getMessageConfig().getMessageByPath("decimal-award-format"));

            String targetName = args[0];
            String awardStr = args[1];

            double award;

            if (awardStr.matches("-?\\d+")) {
                award = Integer.parseInt(awardStr);
            } else {
                player.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.incorrect-number"));
                return false;
            }

            if (award < configManager.getSettingsConfig().getMinAward()) {
                String minimumAward = configManager.getMessageConfig().getMessageByPath("messages.killer-command.minimum-award");
                player.sendMessage(ParseValue.parseWithBraces(minimumAward,
                        new String[]{"MINIMUM_AWARD"}, new String[]{decimalFormat.format(configManager.getSettingsConfig().getMinAward())}));
                return false;
            }

            Economy economy = KillWarrant.getEcon();
            double balance = economy.getBalance(player.getName());

            if (balance < award) {
                player.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.not-enough-money"));
                return false;
            }

            Player target = plugin.getServer().getPlayer(targetName);

            if (!targetIsValid(player, target)) {
                return false;
            }

            makeOrder(target, player, award, economy);
        } else {
            sendUsage(player);
        }

        return true;
    }

    private void makeOrder(Player target, Player client, double award, Economy economy) {
        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.getOrdersConfig().getTargetList().contains(target.getName())) {
            client.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.already-ordered"));
            return;
        }

        if (target.equals(client) && (!configManager.getSettingsConfig().getCanOrderYourself())) {
            client.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.cant-order-yourself"));
            return;
        }

        LocalDateTime currentDate = LocalDateTime.now();

        Order order = new Order(target, client, award, currentDate);
        configManager.getOrdersConfig().addTarget(order);

        new BossBarNotification(plugin).makeBossBar(client.getWorld(), order);

        String ordered = configManager.getMessageConfig().getMessageByPath("messages.killer-command.ordered");
        Bukkit.broadcastMessage(ParseValue.parseWithBraces(ordered,
                new String[]{"TARGET_NAME", "CLIENT_NAME"}, new String[]{target.getName(), client.getName()}));
        OrderManager orderManager = KillWarrant.getOrderManager();
        orderManager.saveOrder(order);

        economy.withdrawPlayer(client, award);
    }

    private boolean targetIsValid(Player client, Player target) {
        ConfigManager configManager = ConfigManager.getInstance();

        if (target == null) {
            client.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.not-exist"));
            return false;
        }
        if (!target.isOnline()) {
            client.sendMessage(configManager.getMessageConfig().getMessageByPath("messages.killer-command.not-online"));
            return false;
        }

        return true;
    }

    private void sendUsage(Player player) {
        ConfigManager configManager = ConfigManager.getInstance();

        configManager.getMessageConfig().getLinesByPath("messages.killer-command.usage").forEach(player::sendMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
