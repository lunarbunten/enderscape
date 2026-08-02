package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeEnchantmentTags {
    public static final TagKey<Enchantment> PREVENTS_DRIFTER_STOMP_DAMAGE = register("prevents_drifter_stomp_damage");

    private static TagKey<Enchantment> register(String name) {
        return TagKey.create(Registries.ENCHANTMENT, Enderscape.id(name));
    }
}