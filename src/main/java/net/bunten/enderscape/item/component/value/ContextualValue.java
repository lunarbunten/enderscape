package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public record ContextualValue(
        float base,
        float glidingMultiplier
) {

    public static final Codec<ContextualValue> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("base").forGetter(ContextualValue::base),
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("gliding_multiplier", 1.0F).forGetter(ContextualValue::glidingMultiplier)
            ).apply(instance, ContextualValue::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ContextualValue> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ContextualValue::base,
            ByteBufCodecs.FLOAT,
            ContextualValue::glidingMultiplier,
            ContextualValue::new
    );

    public Numeric calculate(ServerPlayer player) {
        float value = base;
        if (player.isFallFlying()) value *= glidingMultiplier;
        return new Numeric(value);
    }

    public record Numeric(float value) {
        public float asFloat() {
            return value;
        }

        public int asInteger() {
            return (int) value;
        }
    }
}