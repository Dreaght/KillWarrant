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
            String[] serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            String version = serverVersion[serverVersion.length - 1];
            String nmsPackage = "v1_13_R2";

            if (version.startsWith("v1_12") || version.startsWith("v1_11") || version.startsWith("v1_10") || version.startsWith("v1_9")) {
                nmsPackage = "v1_12_R1";
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "NmsStuff does not have a compatible implementation for this server version.");
            }

            internals = (InternalsProvider) Class.forName(packageName + "." + nmsPackage).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "NmsStuff could not find a valid implementation for this server version.");
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
