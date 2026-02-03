package net.bunten.enderscape.registry;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.component.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.function.UnaryOperator;

public class EnderscapeDataComponents {

    public static final DataComponentType<AttackSounds> ATTACK_SOUNDS = register("attack_sounds", builder -> builder.persistent(AttackSounds.CODEC).networkSynchronized(AttackSounds.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<DashJump> DASH_JUMP = register("dash_jump", builder -> builder.persistent(DashJump.CODEC).networkSynchronized(DashJump.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<ThresholdCounter> THRESHOLD_COUNTER = register("threshold_counter", builder -> builder.persistent(ThresholdCounter.CODEC).networkSynchronized(ThresholdCounter.STREAM_CODEC));
    public static final DataComponentType<Togglable> TOGGLABLE = register("togglable", builder -> builder.persistent(Togglable.CODEC).networkSynchronized(Togglable.STREAM_CODEC));

    public static final DataComponentType<Boolean> ENABLED = register("enabled", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final DataComponentType<Boolean> INCREASE_WITH_DISTANCE = register("increase_with_distance", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<Integer> BACKSTAB_ANGLE = register("backstab_angle", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> ENTITIES_PULLED = register("entities_pulled", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> ENTITIES_PULLED_TO_USE_FUEL = register("entities_pulled_to_use_fuel", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> ENTITY_PULL_RANGE = register("entity_pull_range", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> CURRENT_FUEL = register("current_fuel", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> MAXIMUM_FUEL = register("maximum_fuel", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> FUEL_PER_USE = register("fuel_per_use", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> DISTANCE_TO_INCREASE = register("distance_to_increase", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));


    private static <T> DataComponentType<T> register(String string, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        var type = unaryOperator.apply(DataComponentType.builder()).build();
        RegistryHelper.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Enderscape.id(string), () -> type);
        return type;
    }
}