package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class EnderscapeDamageTypeTags {

    public static final TagKey<DamageType> RUBBLEMITES_CAN_BLOCK = register("rubblemites_can_block");

    private static TagKey<DamageType> register(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Enderscape.id(name));
    }
}