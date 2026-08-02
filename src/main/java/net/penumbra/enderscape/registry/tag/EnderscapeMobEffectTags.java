package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeMobEffectTags {

    public static final TagKey<MobEffect> UNSUPPORTED_WITH_VOIDED_HEALTH = register("unsupported_with_voided_health");

    private static TagKey<MobEffect> register(String name) {
        return TagKey.create(Registries.MOB_EFFECT, Enderscape.id(name));
    }
}