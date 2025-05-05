package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Optional;

public record VeiledTreeConfig(IntProvider log_height, IntProvider branch_count, IntProvider branch_segments, IntProvider leaf_radius, FloatProvider vine_generation_chance, Optional<GrowthConfig> vineConfig, Optional<VeiledLeafPileConfig> leafPileConfig) implements FeatureConfiguration {

    public static final Codec<VeiledTreeConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.codec(1, 64).fieldOf("log_height").forGetter(config -> config.log_height),
            IntProvider.codec(1, 64).fieldOf("branch_count").forGetter(config -> config.branch_count),
            IntProvider.codec(1, 64).fieldOf("branch_segments").forGetter(config -> config.branch_segments),
            IntProvider.codec(1, 64).fieldOf("leaf_radius").forGetter(config -> config.leaf_radius),
            FloatProvider.codec(0, 1).fieldOf("vine_generation_chance").forGetter(config -> config.vine_generation_chance),
            GrowthConfig.CODEC.optionalFieldOf("vine_config").forGetter((config -> config.vineConfig)),
            VeiledLeafPileConfig.CODEC.optionalFieldOf("leaf_pile_config").forGetter((config -> config.leafPileConfig))
    ).apply(instance, VeiledTreeConfig::new));
}