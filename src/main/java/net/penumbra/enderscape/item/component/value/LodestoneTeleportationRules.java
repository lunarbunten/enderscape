package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.network.EnderscapeCodecs;

public record LodestoneTeleportationRules(
        int maximumRange,
        Vec2 maximumEntitySize,
        boolean increaseCostWithDistance,
        boolean restrictTransdimensional
) {

    private static final int DEFAULT_MAXIMUM_RANGE = 2500;
    private static final boolean DEFAULT_INCREASE_COST_WITH_DISTANCE = true;
    private static final boolean DEFAULT_RESTRICT_TRANSDIMENSIONAL = true;
    private static final Vec2 DEFAULT_MAXIMUM_ENTITY_SIZE = new Vec2(4.0F, 3.0F);

    public static final Codec<LodestoneTeleportationRules> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("maximum_range", DEFAULT_MAXIMUM_RANGE).forGetter(LodestoneTeleportationRules::maximumRange),
                    Vec2.CODEC.optionalFieldOf("maximum_entity_size", DEFAULT_MAXIMUM_ENTITY_SIZE).forGetter(LodestoneTeleportationRules::maximumEntitySize),
                    Codec.BOOL.optionalFieldOf("increase_cost_with_distance", DEFAULT_INCREASE_COST_WITH_DISTANCE).forGetter(LodestoneTeleportationRules::increaseCostWithDistance),
                    Codec.BOOL.optionalFieldOf("restrict_transdimensional", DEFAULT_RESTRICT_TRANSDIMENSIONAL).forGetter(LodestoneTeleportationRules::restrictTransdimensional)
            ).apply(instance, LodestoneTeleportationRules::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LodestoneTeleportationRules> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            LodestoneTeleportationRules::maximumRange,
            EnderscapeCodecs.VEC2_STREAM,
            LodestoneTeleportationRules::maximumEntitySize,
            ByteBufCodecs.BOOL,
            LodestoneTeleportationRules::increaseCostWithDistance,
            ByteBufCodecs.BOOL,
            LodestoneTeleportationRules::restrictTransdimensional,
            LodestoneTeleportationRules::new
    );

    public static final LodestoneTeleportationRules DEFAULT = LodestoneTeleportationRules.Builder.create().build();

    public static class Builder {
        private int maximumRange = DEFAULT_MAXIMUM_RANGE;
        private Vec2 maximumEntitySize = DEFAULT_MAXIMUM_ENTITY_SIZE;
        private boolean increaseCostWithDistance = DEFAULT_INCREASE_COST_WITH_DISTANCE;
        private boolean restrictTransdimensional = DEFAULT_RESTRICT_TRANSDIMENSIONAL;

        public static LodestoneTeleportationRules.Builder create() {
            return new LodestoneTeleportationRules.Builder();
        }

        public Builder maximumRange(int maximumRange) {
            this.maximumRange = maximumRange;
            return this;
        }

        public Builder maximumEntitySize(Vec2 maximumEntitySize) {
            this.maximumEntitySize = maximumEntitySize;
            return this;
        }

        public Builder increaseCostWithDistance(boolean increaseCostWithDistance) {
            this.increaseCostWithDistance = increaseCostWithDistance;
            return this;
        }

        public Builder restrictTransdimensional(boolean restrictTransdimensional) {
            this.restrictTransdimensional = restrictTransdimensional;
            return this;
        }

        public LodestoneTeleportationRules build() {
            return new LodestoneTeleportationRules(
                    maximumRange,
                    maximumEntitySize,
                    increaseCostWithDistance,
                    restrictTransdimensional
            );
        }
    }
}