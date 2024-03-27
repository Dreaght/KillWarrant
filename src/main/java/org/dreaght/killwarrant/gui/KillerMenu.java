package org.dreaght.killwarrant.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dreaght.killwarrant.config.ConfigManager;

public class KillerMenu {
    public static void handleMenuCreation(Player player) {
        ConfigManager configManager = ConfigManager.getInstance();

        Inventory inventory = Bukkit.createInventory(player, 36, configManager.getMessageConfig().getMessageByPath("messages.menu.title"));

        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName("§0");
            item.setItemMeta(itemMeta);

            inventory.setItem(i, item);
        }

        ItemStack infoItem = new ItemStack(Material.PAINTING);
        ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.setDisplayName(configManager.getMessageConfig().getMessageByPath("messages.menu.info.name"));

        infoMeta.setLore(configManager.getMessageConfig().getLinesByPath("messages.menu.info.lore"));
        infoItem.setItemMeta(infoMeta);

        inventory.setItem(4, infoItem);

        player.openInventory(inventory);
        MenuManager.addPlayer(player);
    }
}
