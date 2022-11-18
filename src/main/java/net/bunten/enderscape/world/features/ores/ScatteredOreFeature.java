package net.bunten.enderscape.world.features.ores;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class ScatteredOreFeature extends Feature<ScatteredOreFeatureConfig> {
    public ScatteredOreFeature(Codec<ScatteredOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ScatteredOreFeatureConfig> context) {
        ScatteredOreFeatureConfig config = context.config();
        WorldGenLevel world = context.level();
        RandomSource random = context.random();

        BlockPos origin = context.origin();
        BlockPos next = origin;

        int size = config.size().sample(random) / 2;

        for (int i = 0; i < config.tries().sample(random); i++) {
            BlockPos.withinManhattanStream(next, size, size, size).forEach((pos) -> {
                if (world.getBlockState(pos).is(EnderscapeBlocks.ORE_REPLACEABLES)) {
                    world.setBlock(pos, config.state(), 2);
                }
            });

            next = next.offset(config.scatter_increase().sample(random), config.scatter_increase().sample(random), config.scatter_increase().sample(random));
        }

        return true;
    }
}