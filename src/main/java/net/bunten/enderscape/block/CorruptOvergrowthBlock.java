package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionSet;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CorruptOvergrowthBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CorruptOvergrowthBlock> CODEC = simpleCodec(CorruptOvergrowthBlock::new);

    public CorruptOvergrowthBlock(BlockBehaviour.Properties settings) {
        super(false, EnderscapeBlocks.MIRESTONE.get(), EnderscapeBlocks.CORRUPT_PATH.get(), EnderscapeBlockSounds.CORRUPT_OVERGROWTH_FLATTEN, DirectionSet.create().all(), settings);
    }

    @Override
    protected MapCodec<CorruptOvergrowthBlock> codec() {
        return CODEC;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        super.performBonemeal(level, random, origin, state);

        int i = 0;
        int max = Mth.nextInt(random, 1, 2);
        var range = Mth.nextInt(random, 4, 8);

        for (BlockPos pos : BlockPos.randomInCube(random, 5, origin, range)) {
            if (i >= max) break;
            BlockState plant = EnderscapeBlocks.CORRUPT_OVERGROWTH_BONEMEAL_PROVIDER.get().getState(random, pos);
            if (level.isEmptyBlock(pos) && plant.canSurvive(level, pos)) {
                level.setBlock(pos, plant, 2);
                i++;
            }
        }

        AbstractGrowthBlock.generatePatch(level, random, origin, EnderscapeBlocks.CORRUPT_GROWTH.get().defaultBlockState().setValue(StateProperties.FACING, state.getValue(StateProperties.FACING)), Mth.nextInt(random, 3, 10), 0, Mth.nextInt(random, 2, 4));
    }
}