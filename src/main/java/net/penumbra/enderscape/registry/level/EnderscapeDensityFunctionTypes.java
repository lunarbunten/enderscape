package net.penumbra.enderscape.registry.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeDensityFunctionTypes {

    static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0, 1000000.0);

    record DistanceFromCenterGradient(int minDistance, int maxDistance, double fromValue, double toValue) implements DensityFunction.SimpleFunction {
        private static final MapCodec<DistanceFromCenterGradient> DATA_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.INT.fieldOf("min_distance").forGetter(DistanceFromCenterGradient::minDistance),
                                Codec.INT.fieldOf("max_distance").forGetter(DistanceFromCenterGradient::maxDistance),
                                NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(DistanceFromCenterGradient::fromValue),
                                NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(DistanceFromCenterGradient::toValue)
                        )
                        .apply(instance, DistanceFromCenterGradient::new)
        );
        public static final KeyDispatchDataCodec<DistanceFromCenterGradient> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

        @Override
        public double compute(FunctionContext context) {
            float f = (float) context.blockX();
            float h = (float) context.blockZ();

            float distance = Mth.sqrt(f * f + h * h);

            return Mth.clampedMap(distance, minDistance, maxDistance, fromValue, toValue);
        }

        @Override
        public double minValue() {
            return Math.min(fromValue, toValue);
        }

        @Override
        public double maxValue() {
            return Math.max(fromValue, toValue);
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    private static MapCodec<? extends DensityFunction> register(String string, KeyDispatchDataCodec<? extends DensityFunction> dispatch) {
        return Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, Enderscape.id(string), dispatch.codec());
    }

    static {
        register("distance_from_center_gradient", DistanceFromCenterGradient.CODEC);
    }
}