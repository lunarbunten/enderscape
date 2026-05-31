package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VeiledLeafPileConfig(FloatProvider radius, FloatProvider density, IntProvider layers) implements FeatureConfiguration {

    public static final Codec<VeiledLeafPileConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FloatProviders.codec(1, 64).fieldOf("radius").forGetter(config -> config.radius),
            FloatProviders.codec(0, 1).fieldOf("density").forGetter(config -> config.density),
            IntProviders.codec(1, 8).fieldOf("layers").forGetter(config -> config.layers)
    ).apply(instance, VeiledLeafPileConfig::new));
}