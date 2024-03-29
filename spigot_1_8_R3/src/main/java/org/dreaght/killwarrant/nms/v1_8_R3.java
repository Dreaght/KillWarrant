package org.dreaght.killwarrant.nms;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class v1_8_R3 implements InternalsProvider {
    @Override
    public ItemStack getStainedGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE);
    }

    @Override
    public ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setOwner(player.getName());
        skull.setItemMeta(skullMeta);

        return skull;
    }

    @Override
    public Sound getFireballExplode() {
        return Sound.GHAST_FIREBALL;
    }

    @Override
    public Sound getDragonHurt() {
        return Sound.ENDERDRAGON_HIT;
    }
}

