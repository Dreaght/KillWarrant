package org.dreaght.killwarrant.managers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.utils.FileManager;
import org.dreaght.killwarrant.utils.InventorySerializerUtil;

import java.io.*;

public class InventoryStateHandler {
    private static final String FILE_NAME = "inventory_state.base64";

    public static void saveInventory(Inventory inventory, Plugin plugin) {
        try {
            String base64 = InventorySerializerUtil.toBase64(inventory);
            File dataFolder = plugin.getDataFolder();
            File filePath = new File(dataFolder, FILE_NAME);
            FileManager.saveStringToFile(base64, filePath.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Inventory loadInventoryAndDeleteFile(Plugin plugin) {
        try {
            File dataFolder = plugin.getDataFolder();
            File filePath = new File(dataFolder, FILE_NAME);
            String base64 = FileManager.loadStringFromFile(filePath.getPath());
            ItemStack[] contents = InventorySerializerUtil.fromBase64(base64).getContents();
            FileManager.deleteFile(filePath.getPath());
            return createInventory(contents);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Inventory createInventory(ItemStack[] items) {
        if (items == null || items.length == 0) {
            return null;
        }

        int size = items.length;
        System.out.println(size);

        Inventory inventory = Bukkit.createInventory(
                null, size, ConfigManager.getInstance().getMessageConfig().getMessageByPath("messages.menu.title"));
        inventory.setContents(items);
        return inventory;
    }
}

