package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public class LargeCelestialFungusFeatureConfig implements FeatureConfig {
    public static final Codec<LargeCelestialFungusFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.createValidatingCodec(1, 64).fieldOf("height")).forGetter(config -> config.height),
            (Codec.floatRange(1, 64).fieldOf("capRadiusDivision")).forGetter(config -> config.capRadiusDivision),
            (Codec.floatRange(1, 64).fieldOf("stemCapDivision")).forGetter(config -> config.stemCapDivision),
            (Codec.floatRange(0, 1).fieldOf("percentageForCapDrooping")).forGetter(config -> config.percentageForCapDrooping),
            (Codec.floatRange(0, 1).fieldOf("excessVineDiscardChance")).forGetter(config -> config.excessVineDiscardChance),
            (Codec.intRange(1, 256).fieldOf("vineGenerationTries")).forGetter(config -> config.vineGenerationTries),
            (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, LargeCelestialFungusFeatureConfig::new));

    private final IntProvider height;
    private final float capRadiusDivision;
    private final float stemCapDivision;
    private final float percentageForCapDrooping;
    private final float excessVineDiscardChance;
    private final int vineGenerationTries;
    private final int tries;

    public LargeCelestialFungusFeatureConfig(IntProvider height, float capRadiusDivision, float stemCapDivision, float percentageForCapDrooping, float excessVineDiscardChance, int vineGenerationTries, int tries) {
        this.height = height;
        this.capRadiusDivision = capRadiusDivision;
        this.stemCapDivision = stemCapDivision;
        this.percentageForCapDrooping = percentageForCapDrooping;
        this.excessVineDiscardChance = excessVineDiscardChance;
        this.vineGenerationTries = vineGenerationTries;
        this.tries = tries;
    }

    public IntProvider getHeight() {
        return height;
    }

    public float getCapRadiusDivision() {
        return capRadiusDivision;
    }

    public float getStemCapDivision() {
        return stemCapDivision;
    }

    public float getPercentageforCapDrooping() {
        return percentageForCapDrooping;
    }

    public float getExcessVineDiscardChance() {
        return excessVineDiscardChance;
    }

    public int getVineGenerationTries() {
        return vineGenerationTries;
    }

    public int getTries() {
        return tries;
    }
}