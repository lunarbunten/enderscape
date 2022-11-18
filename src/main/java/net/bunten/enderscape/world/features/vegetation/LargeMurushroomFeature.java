package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.world.generator.LargeMurushroomGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LargeMurushroomFeature extends Feature<LargeMurushroomFeatureConfig> {
    public LargeMurushroomFeature(Codec<LargeMurushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeMurushroomFeatureConfig> context) {
        var config = context.config();

        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        var height = config.height().sample(random);
        var cap_radius = config.cap_radius().sample(random);
        var tries = config.tries();
        var direction = Direction.DOWN;

        for (int u = -8; u < 8; u++) {
            var pos2 = pos.below(u);

            if (world.getBlockState(pos2).isAir() && world.getBlockState(pos2.relative(direction.getOpposite())).is(EnderscapeBlocks.CORRUPT_MYCELIUM_BLOCK)) {
                return LargeMurushroomGenerator.generate(direction, world, random, pos2, height, cap_radius, tries);
            }
        }

        return false;
    }
}