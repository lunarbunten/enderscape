package net.bunten.enderscape.registry;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.item.component.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.function.UnaryOperator;

public class EnderscapeDataComponents {

    public static final DataComponentType<AttackSounds> ATTACK_SOUNDS = register("attack_sounds", builder -> builder.persistent(AttackSounds.CODEC).networkSynchronized(AttackSounds.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<DashJump> DASH_JUMP = register("dash_jump", builder -> builder.persistent(DashJump.CODEC).networkSynchronized(DashJump.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<EntityMagnet> ENTITY_MAGNET = register("entity_magnet", builder -> builder.persistent(EntityMagnet.CODEC).networkSynchronized(EntityMagnet.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<LodestoneTeleportation> LODESTONE_TELEPORTATION = register("lodestone_teleportation", builder -> builder.persistent(LodestoneTeleportation.CODEC).networkSynchronized(LodestoneTeleportation.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<FueledTool> FUELED_TOOL = register("fueled_tool", builder -> builder.persistent(FueledTool.CODEC).networkSynchronized(FueledTool.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<StunAttack> STUN_ATTACK = register("stun_attack", builder -> builder.persistent(StunAttack.CODEC).networkSynchronized(StunAttack.STREAM_CODEC));
    public static final DataComponentType<ThresholdCounter> THRESHOLD_COUNTER = register("threshold_counter", builder -> builder.persistent(ThresholdCounter.CODEC).networkSynchronized(ThresholdCounter.STREAM_CODEC));
    public static final DataComponentType<Togglable> TOGGLABLE = register("togglable", builder -> builder.persistent(Togglable.CODEC).networkSynchronized(Togglable.STREAM_CODEC));

    public static final DataComponentType<Boolean> ENABLED = register("enabled", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<Integer> BACKSTAB_ANGLE = register("backstab_angle", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> CURRENT_FUEL = register("current_fuel", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Holder<RubblemiteVariant>> RUBBLEMITE_VARIANT = register(
            "rubblemite/variant", builder -> builder.persistent(RubblemiteVariant.CODEC).networkSynchronized(RubblemiteVariant.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String string, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Enderscape.id(string), unaryOperator.apply(DataComponentType.builder()).build());
    }
}