package org.dreaght.killwarrant.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.dreaght.killwarrant.Config;
import org.dreaght.killwarrant.utils.Order;

import java.util.ArrayList;
import java.util.List;

public class KillerMenu {
    public static void handleMenuCreation(Player player) {
        Config config = new Config();

        Inventory inventory = Bukkit.createInventory(player, 36, config.getMessageByPath("messages.menu.title"));

        List<Order> orders = config.getAllOrders();
        List<ItemStack> targetHeads = getTargetHeads(orders);

        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName("§0");
            item.setItemMeta(itemMeta);

            inventory.setItem(i, item);
        }

        for (int i = 9; i < targetHeads.size() + 9; i++) {
            inventory.setItem(i, targetHeads.get(i - 9));
        }

        ItemStack infoItem = new ItemStack(Material.PAINTING);
        ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.setDisplayName(config.getMessageByPath("messages.menu.info.name"));

        infoMeta.setLore(config.getLinesByPath("messages.menu.info.lore"));
        infoItem.setItemMeta(infoMeta);

        inventory.setItem(4, infoItem);

        player.openInventory(inventory);
    }

    private static List<ItemStack> getTargetHeads(List<Order> orders) {
        List<ItemStack> targetHeads = new ArrayList<>();
        Config config = new Config();

        for (Order order : orders) {
            ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

            skullMeta.setOwner(order.getTargetName());
            skullMeta.setDisplayName(ChatColor.RED + order.getTargetName());

            List<String> lore = new ArrayList<>();
            lore.add(String.format(config.getMessageByPath("messages.killer-command.head-client"), order.getClientName()));
            lore.add(String.format(config.getMessageByPath("messages.killer-command.head-award"), order.getAward()));

            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);

            targetHeads.add(skull);
        }

        return targetHeads;
    }
}
