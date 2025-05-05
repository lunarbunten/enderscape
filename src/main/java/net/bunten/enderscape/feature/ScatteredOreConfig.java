package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public record ScatteredOreConfig(List<OreConfiguration.TargetBlockState> targets, IntProvider tries, IntProvider scatter_increase, IntProvider size) implements FeatureConfiguration {
    public static final Codec<ScatteredOreConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targets),
            IntProvider.codec(1, 200).fieldOf("tries").forGetter(config -> config.tries),
            IntProvider.codec(-5, 5).fieldOf("scatter_increase").forGetter(config -> config.scatter_increase),
            IntProvider.codec(0, 5).fieldOf("size").forGetter(config -> config.size))
            .apply(instance, ScatteredOreConfig::new));
}