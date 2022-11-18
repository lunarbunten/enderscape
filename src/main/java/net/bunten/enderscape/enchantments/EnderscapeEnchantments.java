package net.bunten.enderscape.enchantments;

import com.chocohead.mm.api.ClassTinkerers;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnderscapeEnchantments {

    public static final EnchantmentCategory MIRROR = ClassTinkerers.getEnum(EnchantmentCategory.class, "ENDERSCAPE_MIRROR");

    public static final Enchantment LIGHTSPEED = register("lightspeed", new LightspeedEnchantment(Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final Enchantment TRANSDIMENSIONAL = register("transdimensional", new TransdimensionalEnchantment(Rarity.RARE, EquipmentSlot.MAINHAND));

    private static <T extends Enchantment> T register(String name, T enchantment) {
        return Registry.register(Registry.ENCHANTMENT, Enderscape.id(name), enchantment);
    }
}