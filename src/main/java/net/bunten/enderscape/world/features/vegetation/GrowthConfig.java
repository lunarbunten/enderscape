package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record GrowthConfig(BlockState state, IntProvider base_height, IntProvider added_height, float added_height_chance) implements FeatureConfiguration {
    public static final Codec<GrowthConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (BlockState.CODEC.fieldOf("state")).forGetter(config -> config.state),
            (IntProvider.codec(1, 64).fieldOf("base_height")).forGetter(config -> config.base_height),
            (IntProvider.codec(0, 64).fieldOf("added_height")).forGetter(config -> config.added_height),
            (Codec.floatRange(0, 1).fieldOf("added_height_chance")).forGetter(config -> config.added_height_chance))
            .apply(instance, GrowthConfig::new));

    public GrowthConfig(BlockState state, IntProvider height) {
        this(state, height, ConstantInt.ZERO, 0);
    }
}