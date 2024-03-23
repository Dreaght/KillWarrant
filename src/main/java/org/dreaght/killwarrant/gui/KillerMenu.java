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
import org.dreaght.killwarrant.KillWarrant;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.ParseValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KillerMenu {
    public static void handleMenuCreation(Player player) {
        Config config = KillWarrant.getCfg();

        Inventory inventory = Bukkit.createInventory(player, 36, config.getMessageByPath("messages.menu.title"));

        Set<Order> orders = KillWarrant.getOrderManager().getOrders();

        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName("§0");
            item.setItemMeta(itemMeta);

            inventory.setItem(i, item);
        }

        ItemStack infoItem = new ItemStack(Material.PAINTING);
        ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.setDisplayName(config.getMessageByPath("messages.menu.info.name"));

        infoMeta.setLore(config.getLinesByPath("messages.menu.info.lore"));
        infoItem.setItemMeta(infoMeta);

        inventory.setItem(4, infoItem);

        player.openInventory(inventory);
        MenuManager.addPlayer(player);
    }
}
