package net.bunten.enderscape.world.features.ores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record ScatteredOreFeatureConfig(BlockState state, IntProvider tries, IntProvider scatter_increase, IntProvider size) implements FeatureConfiguration {
    public static final Codec<ScatteredOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (BlockState.CODEC.fieldOf("state")).forGetter(config -> config.state),
            (IntProvider.codec(1, 200).fieldOf("tries")).forGetter(config -> config.tries),
            (IntProvider.codec(-5, 5).fieldOf("scatter_increase")).forGetter(config -> config.scatter_increase),
            (IntProvider.codec(0, 5).fieldOf("size")).forGetter(config -> config.size))
            .apply(instance, ScatteredOreFeatureConfig::new));
}