package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.penumbra.enderscape.block.AbstractGrowthBlock;

public class GrowthFeature extends Feature<GrowthFeature.Config> {
    public GrowthFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        return AbstractGrowthBlock.generate(context.level(), context.origin(), context.random(), context.config());
    }

    public record Config(BlockState state, IntProvider base_height, IntProvider added_height, float added_height_chance) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockState.CODEC.fieldOf("state")).forGetter(config -> config.state),
                (IntProviders.codec(1, 64).fieldOf("base_height")).forGetter(config -> config.base_height),
                (IntProviders.codec(0, 64).fieldOf("added_height")).forGetter(config -> config.added_height),
                (Codec.floatRange(0, 1).fieldOf("added_height_chance")).forGetter(config -> config.added_height_chance))
                .apply(instance, Config::new));

        public Config(BlockState state, IntProvider height) {
            this(state, height, ConstantInt.ZERO, 0);
        }
    }
}