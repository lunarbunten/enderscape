package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record MagniaSpikeConfig(IntProvider height, FloatProvider repulsive_magnia_sprout_placement_chance, int minimum_terrain_depth) implements FeatureConfiguration {

    public static final Codec<MagniaSpikeConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    IntProviders.codec(1, 64).fieldOf("height").forGetter(config -> config.height),
                    FloatProviders.codec(0, 1).fieldOf("repulsive_magnia_sprout_placement_chance").forGetter(config -> config.repulsive_magnia_sprout_placement_chance),
                    Codec.INT.fieldOf("minimum_terrain_depth").forGetter(config -> config.minimum_terrain_depth))
                    .apply(instance, MagniaSpikeConfig::new));
}