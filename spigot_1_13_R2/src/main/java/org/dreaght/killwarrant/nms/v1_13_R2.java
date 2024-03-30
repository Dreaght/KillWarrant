package org.dreaght.killwarrant.nms;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class v1_13_R2 implements InternalsProvider {
    @Override
    public ItemStack getStainedGlass() {
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }

    @Override
    public ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);

        return skull;
    }

    @Override
    public Sound getFireballExplode() {
        return Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
    }

    @Override
    public Sound getDragonHurt() {
        return Sound.ENTITY_ENDER_DRAGON_HURT;
    }
}
