package org.dreaght.killwarrant.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.utils.Order;
import org.dreaght.killwarrant.utils.ParseValue;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuManager {
    private static MenuManager instance;
    private Plugin plugin;
    private Inventory inventory;

    private MenuManager(Plugin plugin) {
        this.plugin = plugin;
        loadContent();
        startUpdating();
        reopenInventoryForPlayers();
    }

    public static void init(Plugin plugin) {
        if (instance == null) {
            instance = new MenuManager(plugin);
        }
    }

    public static MenuManager getInstance() {
        return instance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void reopenInventoryForPlayers() {
        Inventory loadedInventory = InventoryStateHandler.loadInventoryAndDeleteFile(plugin);

        if (loadedInventory == null) {
            System.out.println("null");
            return;
        }

        Set<Player> playersToReopen = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory playerInventory = player.getOpenInventory().getTopInventory();
            if (inventoriesEqual(playerInventory, loadedInventory)) {
                System.out.println("equal");
                playersToReopen.add(player);
            } else {
                System.out.println("not equal");
            }
        }

        for (Player player : playersToReopen) {
            System.out.println("reopening for " + player.getName());
            player.closeInventory();
            player.openInventory(inventory);
        }
    }

    private boolean inventoriesEqual(Inventory inv1, Inventory inv2) {
        if (inv1.getSize() != inv2.getSize()) {
            return false;
        }
        for (int i = 0; i < inv1.getSize(); i++) {
            ItemStack item1 = inv1.getItem(i);
            ItemStack item2 = inv2.getItem(i);
            if ((item1 == null && item2 != null) || (item1 != null && !item1.equals(item2))) {
                return false;
            }
        }
        return true;
    }

    public void startUpdating() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Set<Order> orders = OrderManager.getInstance().getOrders();

                List<ItemStack> targetHeads = getTargetHeads(orders);

                if (targetHeads.isEmpty()) {
                    for (int i = 9; i < 35; i++) {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }

                for (int i = 9; i < targetHeads.size() + 9; i++) {
                    inventory.setItem(i, targetHeads.get(i - 9));
                }

                for (int i = 9 + targetHeads.size(); i < 35; i++) {
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
            }
        };

        runnable.runTaskTimer(plugin, 0, 20);
    }

    public void loadContent() {
        ConfigManager configManager = ConfigManager.getInstance();
        inventory = Bukkit.createInventory(null, 36, ConfigManager.getInstance().getMessageConfig().getMessageByPath("messages.menu.title"));

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
    }

    private List<ItemStack> getTargetHeads(Set<Order> orders) {
        List<ItemStack> targetHeads = new ArrayList<>();
        ConfigManager configManager = ConfigManager.getInstance();

        DecimalFormat decimalAwardFormat = new DecimalFormat(configManager.getSettingsConfig().getDecimalAwardFormat());
        DecimalFormat decimalLocFormat = new DecimalFormat(configManager.getSettingsConfig().getDecimalLocationFormat());

        for (Order order : orders) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(order.getTargetName()));

            LocalDateTime date = order.getDate();
            LocalDateTime currentDate = LocalDateTime.now();

            skullMeta.setDisplayName(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killer-command.head.target"),
                    new String[]{"TARGET_NAME", "TIME_LEFT"}, new Object[]{order.getTargetName(), OrderManager.getInstance().timeLeft(date, currentDate)}));

            List<String> lore = new ArrayList<>();
            lore.add(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killer-command.head.client"),
                    new String[]{"CLIENT_NAME"}, new String[]{order.getClientName()}));
            lore.add(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killer-command.head.award"),
                    new String[]{"AWARD"}, new Object[]{decimalAwardFormat.format(order.getAward())}));

            String location = decimalLocFormat.format(order.getTargetLocation().getX()) + " " +
                    decimalLocFormat.format(order.getTargetLocation().getY()) + " " +
                    decimalLocFormat.format(order.getTargetLocation().getZ());

            long differenceInSeconds = java.time.Duration.between(date, currentDate).getSeconds();
            int seconds = configManager.getSettingsConfig().getLocationUpdatePeriod();
            long remainder = seconds - (differenceInSeconds % seconds) - 1;

            lore.add(ParseValue.parseWithBraces(configManager.getMessageConfig().getMessageByPath("messages.killer-command.head.location"),
                    new String[]{"LOCATION", "COUNT"}, new Object[]{location, remainder}));

            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);

            targetHeads.add(skull);
        }

        return targetHeads;
    }
}
