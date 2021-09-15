package net.enderscape.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnduranceEnchantment extends Enchantment {
    public EnduranceEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.VANISHABLE, new EquipmentSlot[]{
                EquipmentSlot.MAINHAND
        });
    }

    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    public int getMaxPower(int level) {
        return getMinPower(level) + 15;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof MirrorItem;
    }
}