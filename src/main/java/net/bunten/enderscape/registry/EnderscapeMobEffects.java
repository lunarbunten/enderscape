package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.effect.LowGravityEffect;
import net.bunten.enderscape.effect.StunnedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.function.Supplier;

public class EnderscapeMobEffects {

    public static final Holder<MobEffect> LOW_GRAVITY = register("low_gravity", () -> new LowGravityEffect()
            .addAttributeModifier(Attributes.GRAVITY, Enderscape.id("effect.low_gravity.gravity"), -0.6, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, Enderscape.id("effect.low_gravity.fall_damage_multiplier"), 0.6, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .withSoundOnAdded(EnderscapeEventSounds.APPLY_EFFECT_LOW_GRAVITY)
    );

    public static final Holder<MobEffect> STUNNED = register("stunned", StunnedEffect::new);

    private static Holder<MobEffect> register(String name, Supplier<MobEffect> effect) {
        return RegistryHelper.registerForHolder(BuiltInRegistries.MOB_EFFECT, Enderscape.id(name), effect);
    }

    public static boolean isStunned(Entity entity) {
        return entity instanceof LivingEntity living && !living.isSpectator() && living.hasEffect(EnderscapeMobEffects.STUNNED);
    }
}