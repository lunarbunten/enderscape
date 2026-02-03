package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record LodestoneTeleportationCost(
        boolean increaseWithDistance,
        int distanceToIncrease
) {

    public static final boolean DEFAULT_INCREASE_WITH_DISTANCE = true;
    public static final int DEFAULT_DISTANCE_TO_INCREASE = 500;

    public static final Codec<LodestoneTeleportationCost> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("increase_with_distance", DEFAULT_INCREASE_WITH_DISTANCE).forGetter(LodestoneTeleportationCost::increaseWithDistance),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("distance_to_increase", DEFAULT_DISTANCE_TO_INCREASE).forGetter(LodestoneTeleportationCost::distanceToIncrease)
            ).apply(instance, LodestoneTeleportationCost::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LodestoneTeleportationCost> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            LodestoneTeleportationCost::increaseWithDistance,
            ByteBufCodecs.INT,
            LodestoneTeleportationCost::distanceToIncrease,
            LodestoneTeleportationCost::new
    );

    public static final LodestoneTeleportationCost DEFAULT = Builder.create().build();

    public static class Builder {
        private boolean increaseWithDistance = DEFAULT_INCREASE_WITH_DISTANCE;
        private int distanceToIncrease = DEFAULT_DISTANCE_TO_INCREASE;

        public static Builder create() {
            return new Builder();
        }

        public Builder link(boolean increaseWithDistance) {
            this.increaseWithDistance = increaseWithDistance;
            return this;
        }

        public Builder distanceToIncrease(int distanceToIncrease) {
            this.distanceToIncrease = distanceToIncrease;
            return this;
        }

        public LodestoneTeleportationCost build() {
            return new LodestoneTeleportationCost(
                    increaseWithDistance,
                    distanceToIncrease
            );
        }
    }
}