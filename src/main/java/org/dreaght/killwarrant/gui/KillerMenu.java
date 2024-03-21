package org.dreaght.killwarrant.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.utils.Order;

import java.util.ArrayList;
import java.util.List;

public class KillerMenu {
    public static void handleMenuCreation(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 36, ChatColor.DARK_GRAY + "Kill Warrants");

        List<Order> orders = new Config().getAllOrders();
        List<ItemStack> targetHeads = getTargetHeads(orders);

        for (int i = 0; i < targetHeads.size(); i++) {
            inventory.setItem(i, targetHeads.get(i));
        }

        player.openInventory(inventory);
    }

    private static List<ItemStack> getTargetHeads(List<Order> orders) {
        List<ItemStack> targetHeads = new ArrayList<>();

        for (Order order : orders) {
            ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

            skullMeta.setOwner(order.getTargetName());
            skullMeta.setDisplayName(ChatColor.RED + order.getTargetName());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Client: " + ChatColor.GREEN + order.getClientName());
            lore.add(ChatColor.GRAY + "Award: " + ChatColor.GOLD + order.getAward());

            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);

            targetHeads.add(skull);
        }

        return targetHeads;
    }
}
