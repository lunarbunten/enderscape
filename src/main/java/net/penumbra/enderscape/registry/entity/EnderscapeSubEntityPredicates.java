package net.penumbra.enderscape.registry.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.penumbra.enderscape.entity.rubblemite.RubblemiteVariantPredicate;

public class EnderscapeSubEntityPredicates {

    public static final Codec<RubblemiteVariantPredicate> RUBBLEMITE_VARIANT_PREDICATE = register("rubblemite", RubblemiteVariantPredicate.CODEC);

    private static <T extends EntitySubPredicate> Codec<T> register(String string, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, string, codec);
    }
}