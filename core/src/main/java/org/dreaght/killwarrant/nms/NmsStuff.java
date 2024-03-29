package org.dreaght.killwarrant.nms;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class NmsStuff {
    static InternalsProvider internals;

    static {
        try {
            String packageName = NmsStuff.class.getPackage().getName();
            String internalsName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            internals = (InternalsProvider) Class.forName(packageName + "." + internalsName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "ItemUtil could not find a valid implementation for this server version.");
        }
    }

    public static ItemStack getStainedGlass() {
        return internals.getStainedGlass();
    }

    public static ItemStack getSkull(Player player) {
        return internals.getSkull(player);
    }

    public static Sound getFireballExplode() {
        return internals.getFireballExplode();
    }

    public static Sound getDragonHurt() {
        return internals.getDragonHurt();
    }
}
