package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ScatteredOreFeature extends Feature<ScatteredOreConfig> {
    public ScatteredOreFeature(Codec<ScatteredOreConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ScatteredOreConfig> context) {
        ScatteredOreConfig config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        BlockPos next = context.origin();

        int size = config.size().sample(random) / 2;
        boolean bl = false;

        for (int i = 0; i < config.tries().sample(random); i++) {
            for (BlockPos pos : BlockPos.withinManhattanStream(next, size, size, size).toList()) {
                for (OreConfiguration.TargetBlockState target : config.targets()) {
                    if (target.target.test(level.getBlockState(pos), random)) {
                        level.setBlock(pos, target.state, 2);
                        bl = true;
                    }
                }
            }

            next = next.offset(config.scatter_increase().sample(random), config.scatter_increase().sample(random), config.scatter_increase().sample(random));
        }

        return bl;
    }
}