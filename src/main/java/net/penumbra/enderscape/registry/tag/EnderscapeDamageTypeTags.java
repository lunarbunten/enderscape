package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeDamageTypeTags {

    public static final TagKey<DamageType> RUBBLEMITES_CAN_BLOCK = register("rubblemites_can_block");
    public static final TagKey<DamageType> IS_VOID = register("is_void");
    public static final TagKey<DamageType> VOIDS_HEALTH = register("voids_health");

    private static TagKey<DamageType> register(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Enderscape.id(name));
    }
}