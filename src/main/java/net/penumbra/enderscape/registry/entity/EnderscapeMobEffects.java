package net.penumbra.enderscape.registry.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.effect.*;
import net.penumbra.enderscape.registry.sound.EnderscapeMobEffectSounds;

public class EnderscapeMobEffects {

    public static final Holder<MobEffect> LOW_GRAVITY = register("low_gravity", new LowGravityEffect()
            .addAttributeModifier(Attributes.GRAVITY, Enderscape.id("effect.low_gravity.gravity"), -0.6, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, Enderscape.id("effect.low_gravity.fall_damage_multiplier"), 0.9, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .withSoundOnAdded(EnderscapeMobEffectSounds.LOW_GRAVITY_APPLY)
    );

    public static final Holder<MobEffect> STUNNED = register("stunned", new StunnedEffect());
    public static final Holder<MobEffect> VOID_CORRUPTION = register("void_corruption", new VoidCorruptionEffect());
    public static final Holder<MobEffect> VOID_RESISTANCE = register("void_resistance", new VoidResistanceEffect());
    public static final Holder<MobEffect> VOID_PURIFICATION = register("void_purification", new VoidPurificationEffect());

    private static Holder.Reference<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Enderscape.id(name), effect);
    }

    public static boolean isStunned(Entity entity) {
        if (entity instanceof LivingEntity living && !living.isSpectator()) {
            return living.hasEffect(EnderscapeMobEffects.STUNNED) || (living.level().isClientSide() && living.hasAttached(EnderscapeAttachments.STUN_TICKS));
        } else {
            return false;
        }
    }
}