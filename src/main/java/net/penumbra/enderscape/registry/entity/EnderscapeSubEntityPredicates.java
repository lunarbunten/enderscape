package net.penumbra.enderscape.registry.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.penumbra.enderscape.entity.rubblemite.RubblemiteVariantPredicate;

public class EnderscapeSubEntityPredicates {

    public static final MapCodec<RubblemiteVariantPredicate> RUBBLEMITE_VARIANT_PREDICATE = register("rubblemite", RubblemiteVariantPredicate.CODEC);

    private static <T extends EntitySubPredicate> MapCodec<T> register(String string, MapCodec<T> mapCodec) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, string, mapCodec);
    }
}