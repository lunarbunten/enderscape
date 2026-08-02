package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;

public record GlidingBasedValue(
        float base,
        float glidingMultiplier
) {

    public static final Codec<GlidingBasedValue> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("base").forGetter(GlidingBasedValue::base),
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("gliding_multiplier", 1.0F).forGetter(GlidingBasedValue::glidingMultiplier)
            ).apply(instance, GlidingBasedValue::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GlidingBasedValue> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            GlidingBasedValue::base,
            ByteBufCodecs.FLOAT,
            GlidingBasedValue::glidingMultiplier,
            GlidingBasedValue::new
    );

    public Numeric calculate(LivingEntity player) {
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