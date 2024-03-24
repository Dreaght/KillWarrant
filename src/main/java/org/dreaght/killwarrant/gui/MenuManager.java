package org.dreaght.killwarrant.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.ParseValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuManager {
    private static final Set<Player> playersViewingMenu = new HashSet<>();

    public static void updateLocForAllMenu(long count) {
        Set<Order> orders = KillWarrant.getOrderManager().getOrders();
        
        List<ItemStack> targetHeads = getTargetHeads(orders, count);
        
        playersViewingMenu.forEach(player -> {
            Inventory inventory = player.getOpenInventory().getTopInventory();

            if (targetHeads.isEmpty()) {
                for (int i = 9; i < 35; i++) {
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
            }

            for (int i = 9; i < targetHeads.size() + 9; i++) {
                inventory.setItem(i, targetHeads.get(i - 9));
            }

            player.updateInventory();
        });
    }
    
    public static boolean contains(Player player) {
        return playersViewingMenu.contains(player);
    }
    
    public static void addPlayer(Player player) {
        playersViewingMenu.add(player);
    }
    
    public static void removePlayer(Player player) {
        playersViewingMenu.remove(player);
    }

    private static List<ItemStack> getTargetHeads(Set<Order> orders, long count) {
        List<ItemStack> targetHeads = new ArrayList<>();
        Config config = KillWarrant.getCfg();

        DecimalFormat decimalAwardFormat = new DecimalFormat(config.getMessageByPath("decimal-award-format"));
        DecimalFormat decimalLocFormat = new DecimalFormat(config.getMessageByPath("decimal-location-format"));

        for (Order order : orders) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(order.getTargetName())); // Set player name directly

            skullMeta.setDisplayName(ChatColor.RED + order.getTargetName());

            List<String> lore = new ArrayList<>();
            lore.add(ParseValue.parseWithBraces(config.getMessageByPath("messages.killer-command.head-client"),
                    new String[]{"CLIENT_NAME"}, new String[]{order.getClientName()}));
            lore.add(ParseValue.parseWithBraces(config.getMessageByPath("messages.killer-command.head-award"),
                    new String[]{"AWARD"}, new Object[]{decimalAwardFormat.format(order.getAward())}));

            String location = decimalLocFormat.format(order.getTargetLocation().getX()) + " " +
                    decimalLocFormat.format(order.getTargetLocation().getY()) + " " +
                    decimalLocFormat.format(order.getTargetLocation().getZ());

            lore.add(ParseValue.parseWithBraces(config.getMessageByPath("messages.killer-command.head-location"),
                    new String[]{"LOCATION", "COUNT"}, new Object[]{location, count}));

            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);

            targetHeads.add(skull);
        }

        return targetHeads;
    }
}
