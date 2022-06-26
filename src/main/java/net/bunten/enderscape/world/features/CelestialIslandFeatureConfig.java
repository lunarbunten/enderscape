package net.bunten.enderscape.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CelestialIslandFeatureConfig implements FeatureConfig {
    public static final Codec<CelestialIslandFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.createValidatingCodec(1, 64).fieldOf("width")).forGetter(config -> config.width),
            (IntProvider.createValidatingCodec(1, 64).fieldOf("height")).forGetter(config -> config.height),
            (Codec.floatRange(0, 1).fieldOf("murushroomChance")).forGetter(config -> config.murushroomChance))
            .apply(instance, CelestialIslandFeatureConfig::new));

    private final IntProvider width;
    private final IntProvider height;
    private final float murushroomChance;

    public CelestialIslandFeatureConfig(IntProvider width, IntProvider height, float murushroomChance) {
        this.width = width;
        this.height = height;
        this.murushroomChance = murushroomChance;
    }

    public IntProvider getWidth() {
        return width;
    }

    public IntProvider getHeight() {
        return height;
    }

    public float getMurushroomChance() {
        return murushroomChance;
    }
}