package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

import java.util.function.UnaryOperator;

public class EnderscapeEnchantmentEffectComponents {

    public static final DataComponentType<EnchantmentValueEffect> MIRROR_DISTANCE_FOR_COST_INCREASE = register("mirror_distance_for_cost_increase", builder -> builder.persistent(EnchantmentValueEffect.CODEC));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Enderscape.id(name), operator.apply(DataComponentType.builder()).build());
    }
}