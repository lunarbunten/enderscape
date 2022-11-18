package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@Mixin(EnchantmentCategory.class)
public abstract class EnchantmentCategoryMixin {
    
    @Shadow
    public abstract boolean canEnchant(Item item);
}