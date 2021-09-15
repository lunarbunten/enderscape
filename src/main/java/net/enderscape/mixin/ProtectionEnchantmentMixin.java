package net.enderscape.mixin;

import net.enderscape.registry.EndItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin extends Enchantment {
    protected ProtectionEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Unique
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.getItem() == EndItems.DRIFT_BOOTS && this == Enchantments.FEATHER_FALLING) {
            return false;
        }
        return super.isAcceptableItem(stack);
    }
}