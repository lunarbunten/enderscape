package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.feature.FeatureConfig;

public class CelestialVegetationFeatureConfig implements FeatureConfig {
    public static final Codec<CelestialVegetationFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (Codec.intRange(1, 64).fieldOf("horizontalRange")).forGetter(config -> config.horizontalRange),
            (Codec.intRange(1, 64).fieldOf("verticalRange")).forGetter(config -> config.verticalRange),
            (Codec.intRange(1, 64).fieldOf("verticalCheckRange")).forGetter(config -> config.verticalCheckRange),
            (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, CelestialVegetationFeatureConfig::new));

    public final int horizontalRange;
    public final int verticalRange;
    public final int verticalCheckRange;
    public final int tries;

    public CelestialVegetationFeatureConfig(int horizontalRange, int verticalRange, int verticalCheckRange, int tries) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.verticalCheckRange = verticalCheckRange;
        this.tries = tries;
    }
}