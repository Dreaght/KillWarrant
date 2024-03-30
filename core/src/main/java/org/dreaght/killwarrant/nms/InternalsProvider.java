package org.dreaght.killwarrant.nms;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

interface InternalsProvider {
    ItemStack getStainedGlass();

    ItemStack getSkull(Player player);

    Sound getFireballExplode();

    Sound getDragonHurt();
}
