package net.bunten.enderscape.registry;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariantPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class EnderscapeSubEntityPredicates {

    public static final MapCodec<RubblemiteVariantPredicate> RUBBLEMITE_VARIANT_PREDICATE = register("rubblemite", RubblemiteVariantPredicate.CODEC);

    private static <T extends EntitySubPredicate> MapCodec<T> register(String string, MapCodec<T> mapCodec) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, string, mapCodec);
    }
}