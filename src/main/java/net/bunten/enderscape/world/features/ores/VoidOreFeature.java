package net.bunten.enderscape.world.features.ores;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class VoidOreFeature extends Feature<BlockStateConfiguration> {
    public VoidOreFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        boolean result = false;

        BlockStateConfiguration config = context.config();
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        for (int i = 0; i < 30; i++) {
            BlockPos pos = BlockUtil.random(origin, random, 8, 4, 8);
            if (world.getBlockState(pos).is(EnderscapeBlocks.ORE_REPLACEABLES) && world.isEmptyBlock(pos.below()) && BlockUtil.hasTerrainDepth(world, pos, 16, Direction.UP)) {
                world.setBlock(pos, config.state, 2);
                result = true;
            }
        }

        return result;
    }
}