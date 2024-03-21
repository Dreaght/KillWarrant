package org.dreaght.killwarrant.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.Config;
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
        if (args.length == 0) {
            KillerMenu.handleMenuCreation(player);
        } else if (args.length == 2) {
            String targetName = args[0];
            String awardStr = args[1];

            int award;

            if (awardStr.matches("-?\\d+")) {
                award = Integer.parseInt(awardStr);
            } else {
                player.sendMessage(ChatColor.RED + "Input correct number as award!");
                return false;
            }

            if (award < new Config().getMinAward()) {
                player.sendMessage(ChatColor.RED + "KillWarrant" + ChatColor.GRAY + " >> "
                        + ChatColor.YELLOW + "Minimum award: " + ChatColor.GOLD + new Config().getMinAward());
                return false;
            }

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
            client.sendMessage(ChatColor.RED + "You can't order it due to it has been already ordered by someone.");
            return;
        }

        Order order = new Order(targetName, client.getDisplayName(), award);
        config.addTarget(order);
    }

    private boolean targetIsValid(Player client, Player target) {
        if (target == null) {
            client.sendMessage("This player doesn't exist.");
            return false;
        }
        if (!target.isOnline()) {
            client.sendMessage("This player must be online.");
            return false;
        }

        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.RED + "KillWarrant" + ChatColor.GRAY + " >> "
                + ChatColor.RED + "/killer" +
                ChatColor.DARK_GRAY + " — " + ChatColor.WHITE + "Open Menu");
        player.sendMessage(ChatColor.RED + "KillWarrant" + ChatColor.GRAY + " >> "
                + ChatColor.RED + "/killer <target> <award>" +
                ChatColor.DARK_GRAY + " — " + ChatColor.WHITE + "Order the player's head");
        player.sendMessage(ChatColor.RED + "KillWarrant" + ChatColor.GRAY + " >> "
                + ChatColor.YELLOW + "Minimum award: " + ChatColor.GOLD + new Config().getMinAward());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
