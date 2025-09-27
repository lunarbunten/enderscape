package net.bunten.enderscape.registry;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.item.component.DashJump;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.animal.frog.FrogVariant;

import java.util.function.UnaryOperator;

public class EnderscapeDataComponents {

    public static final DataComponentType<DashJump> DASH_JUMP = register("dash_jump", builder -> builder.persistent(DashJump.CODEC).networkSynchronized(DashJump.STREAM_CODEC).cacheEncoding());

    public static final DataComponentType<Boolean> ENABLED = register("enabled", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final DataComponentType<Integer> ENTITIES_PULLED = register("entities_pulled", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> ENTITIES_PULLED_TO_USE_FUEL = register("entities_pulled_to_use_fuel", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> ENTITY_PULL_RANGE = register("entity_pull_range", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> DISTANCE_FOR_COST_TO_INCREASE = register("distance_for_cost_to_increase", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> CURRENT_NEBULITE_FUEL = register("current_nebulite_fuel", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> MAXIMUM_NEBULITE_FUEL = register("maximum_nebulite_fuel", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> NEBULITE_FUEL_PER_USE = register("nebulite_fuel_per_use", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Holder<RubblemiteVariant>> RUBBLEMITE_VARIANT = register(
            "rubblemite/variant", builder -> builder.persistent(RubblemiteVariant.CODEC).networkSynchronized(RubblemiteVariant.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String string, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Enderscape.id(string), unaryOperator.apply(DataComponentType.builder()).build());
    }
}