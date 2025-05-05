package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VeiledLeafPileConfig(FloatProvider radius, FloatProvider density, IntProvider layers) implements FeatureConfiguration {

    public static final Codec<VeiledLeafPileConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FloatProvider.codec(1, 64).fieldOf("radius").forGetter(config -> config.radius),
            FloatProvider.codec(0, 1).fieldOf("density").forGetter(config -> config.density),
            IntProvider.codec(1, 8).fieldOf("layers").forGetter(config -> config.layers)
    ).apply(instance, VeiledLeafPileConfig::new));
}