package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CelestialGrowthFeatureConfig implements FeatureConfig {
    public static final Codec<CelestialGrowthFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (Codec.intRange(1, 64).fieldOf("horizontalRange")).forGetter(config -> config.horizontalRange),
            (Codec.intRange(1, 64).fieldOf("verticalRange")).forGetter(config -> config.verticalRange),
            (Codec.intRange(1, 64).fieldOf("verticalCheckRange")).forGetter(config -> config.verticalCheckRange),
            (IntProvider.createValidatingCodec(1, 64).fieldOf("baseHeight")).forGetter(config -> config.baseHeight),
            (IntProvider.createValidatingCodec(1, 64).fieldOf("additionalHeight")).forGetter(config -> config.additionalHeight),
            (Codec.floatRange(0, 1).fieldOf("addedHeightChance")).forGetter(config -> Float.valueOf(config.addedHeightChance)),
            (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, CelestialGrowthFeatureConfig::new));

    private final int horizontalRange;
    private final int verticalRange;
    private final int verticalCheckRange;
    private final IntProvider baseHeight;
    private final IntProvider additionalHeight;
    public final float addedHeightChance;
    private final int tries;

    public CelestialGrowthFeatureConfig(int horizontalRange, int verticalRange, int verticalCheckRange, IntProvider baseHeight, IntProvider additionalHeight, float addedHeightChance, int tries) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.verticalCheckRange = verticalCheckRange;
        this.baseHeight = baseHeight;
        this.additionalHeight = additionalHeight;
        this.addedHeightChance = addedHeightChance;
        this.tries = tries;
    }

    public int getHorizontalRange() {
        return horizontalRange;
    }

    public int getVerticalRange() {
        return verticalRange;
    }

    public int getVerticalCheckRange() {
        return verticalCheckRange;
    }

    public IntProvider getBaseHeight() {
        return baseHeight;
    }

    public IntProvider getAdditionalHeight() {
        return additionalHeight;
    }

    public float getAddedHeightChance() {
        return addedHeightChance;
    }

    public int getTries() {
        return tries;
    }
}