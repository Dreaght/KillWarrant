package org.dreaght.killwarrant.manager;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.util.FileManager;
import org.dreaght.killwarrant.util.InventorySerializerUtil;

import java.io.*;

public class InventoryStateHandler {
    private static final String FILE_NAME = "inventory_state.base64";

    public static void saveInventory(Inventory inventory, Plugin plugin) {
        try {
            String base64 = InventorySerializerUtil.toBase64(inventory);
            File cacheFolder = new File(plugin.getDataFolder(), "cache");
            if (!cacheFolder.exists()) {
                cacheFolder.mkdirs();
            }
            File filePath = new File(cacheFolder, FILE_NAME);
            FileManager.saveStringToFile(base64, filePath.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Inventory loadInventoryAndDeleteFile(Plugin plugin) {
        try {
            File cacheFolder = new File(plugin.getDataFolder(), "cache");
            File filePath = new File(cacheFolder, FILE_NAME);
            if (!filePath.exists()) {
                return null;
            }
            String base64 = FileManager.loadStringFromFile(filePath.getPath());
            ItemStack[] contents = InventorySerializerUtil.fromBase64(base64).getContents();
            FileManager.deleteFile(filePath.getPath());
            if (cacheFolder.isDirectory() && cacheFolder.list().length == 0) {
                cacheFolder.delete();
            }
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

        Inventory inventory = Bukkit.createInventory(
                null, size, ConfigManager.getInstance().getMessageConfig().getMessageByPath("messages.menu.title"));
        inventory.setContents(items);
        return inventory;
    }
}

