package net.bunten.enderscape.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CelestialIslandFeatureConfig(IntProvider width, IntProvider height, float murushroom_chance) implements FeatureConfiguration {
    public static final Codec<CelestialIslandFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("width")).forGetter(config -> config.width),
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(config -> config.height),
            (Codec.floatRange(0, 1).fieldOf("murushroom_chance")).forGetter(config -> config.murushroom_chance))
            .apply(instance, CelestialIslandFeatureConfig::new));
}