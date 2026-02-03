package net.bunten.enderscape.item;

import net.minecraft.world.item.Item;

public class EnchantableItem extends Item {
    public EnchantableItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
