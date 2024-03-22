package org.dreaght.killwarrant.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.gui.BossBarManager;
import org.dreaght.killwarrant.gui.KillerMenu;
import org.dreaght.killwarrant.utils.Order;

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
        Config config = new Config();

        if (args.length == 0) {
            KillerMenu.handleMenuCreation(player);
        } else if (args.length == 2) {
            String targetName = args[0];
            String awardStr = args[1];

            int award;

            if (awardStr.matches("-?\\d+")) {
                award = Integer.parseInt(awardStr);
            } else {
                player.sendMessage(config.getMessageByPath("messages.killer-command.incorrect-number"));
                return false;
            }

            if (award < new Config().getMinAward()) {
                player.sendMessage(String.format(config.getMessageByPath("messages.killer-command.minimum-award"),
                        config.getMinAward()));
                return false;
            }

            Economy economy = KillWarrant.getEcon();
            int balance = Integer.parseInt(
                    economy.format(economy.getBalance(player.getName()))
                            .replace("$", "").replace(",", ""));

            if (balance < award) {
                player.sendMessage(config.getMessageByPath("messages.killer-command.not-enough-money"));
                return false;
            }

            economy.withdrawPlayer(player, award);

            Player target = plugin.getServer().getPlayer(targetName);

            if (!targetIsValid(player, target)) {
                return false;
            }

            makeOrder(targetName, player, award);
        } else {
            sendUsage(player);
        }

        return true;
    }

    private void makeOrder(String targetName, Player client, int award) {
        Config config = new Config();

        if (config.getTargetList().contains(targetName)) {
            client.sendMessage(config.getMessageByPath("messages.killer-command.already-ordered"));
            return;
        }

        Order order = new Order(targetName, client.getName(), award);
        config.addTarget(order);

        new BossBarManager(plugin).makeBossBar(client.getWorld(), order);
        Bukkit.broadcastMessage(String.format(config.getMessageByPath("messages.killer-command.ordered"),
                targetName, client.getName()));
    }

    private boolean targetIsValid(Player client, Player target) {
        Config config = new Config();

        if (target == null) {
            client.sendMessage(config.getMessageByPath("messages.killer-command.not-exist"));
            return false;
        }
        if (!target.isOnline()) {
            client.sendMessage(config.getMessageByPath("messages.killer-command.not-online"));
            return false;
        }

        return true;
    }

    private void sendUsage(Player player) {
        Config config = new Config();

        String message = "";
        for (String line : config.getLinesByPath("messages.menu.info.lore")) {
            message += line.replace("{0}", String.valueOf(config.getMinAward())) + "\n";
        }

        player.sendMessage(message);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
