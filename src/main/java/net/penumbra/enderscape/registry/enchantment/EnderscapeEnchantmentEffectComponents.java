package net.penumbra.enderscape.registry.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.Enderscape;

import java.util.List;
import java.util.function.UnaryOperator;

public class EnderscapeEnchantmentEffectComponents {

    public static final DataComponentType<EnchantmentValueEffect> LODESTONE_TELEPORTATION_MAXIMUM_RANGE = register("lodestone_teleportation_maximum_range", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    public static final DataComponentType<EnchantmentValueEffect> STUN_ATTACK_COOLDOWN_TIME = register("stun_attack_cooldown_time", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    public static final DataComponentType<EnchantmentValueEffect> STUN_ATTACK_EFFECT_DURATION = register("stun_attack_effect_duration", builder -> builder.persistent(EnchantmentValueEffect.CODEC));

    public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> INTEGER_COUNTER_THRESHOLD = register("integer_counter_threshold", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC).listOf()));
    public static final DataComponentType<List<Holder<SoundEvent>>> STUN_ATTACK_SOUND = register("stun_attack_sound", builder -> builder.persistent(SoundEvent.CODEC.listOf()));
    public static final DataComponentType<List<Vec2>> AREA_OF_EFFECT_STUN_ATTACK_RADIUS = register("area_of_effect_stun_attack_radius", builder -> builder.persistent(Vec2.CODEC.listOf()));

    public static final DataComponentType<Unit> LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL = register("lodestone_teleportation_enable_transdimensional", builder -> builder.persistent(Unit.CODEC));
    public static final DataComponentType<Unit> MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES = register("magnet_enable_deposit_into_bundles", builder -> builder.persistent(Unit.CODEC));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Enderscape.id(name), operator.apply(DataComponentType.builder()).build());
    }
}