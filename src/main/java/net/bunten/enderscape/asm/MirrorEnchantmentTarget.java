package net.bunten.enderscape.asm;

import net.bunten.enderscape.items.MirrorItem;
import net.bunten.enderscape.mixin.EnchantmentCategoryMixin;
import net.minecraft.world.item.Item;

public class MirrorEnchantmentTarget extends EnchantmentCategoryMixin {
    
    @Override
    public boolean canEnchant(Item item) {
        return item instanceof MirrorItem;
    }
}