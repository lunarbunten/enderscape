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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CelestialOvergrowthBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CelestialOvergrowthBlock> CODEC = simpleCodec(CelestialOvergrowthBlock::new);

    public CelestialOvergrowthBlock(Properties settings) {
        super(true, Blocks.END_STONE, EnderscapeBlocks.CELESTIAL_PATH, EnderscapeBlockSounds.CELESTIAL_OVERGROWTH_FLATTEN, DirectionSet.create().up(), settings);
    }

    @Override
    protected MapCodec<CelestialOvergrowthBlock> codec() {
        return CODEC;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        super.performBonemeal(level, random, origin, state);

        int i = 0;
        int max = Mth.nextInt(random, 1, 3);
        var range = Mth.nextInt(random, 4, 8);

        for (BlockPos pos : BlockPos.randomInCube(random, 8, origin, range)) {
            if (i >= max) break;
            BlockState plant = EnderscapeBlocks.CELESTIAL_OVERGROWTH_BONEMEAL_PROVIDER.getState(level, random, pos);
            if (level.isEmptyBlock(pos) && plant.canSurvive(level, pos)) {
                level.setBlock(pos, plant, 2);
                i++;
            }
        }

        AbstractGrowthBlock.generatePatch(level, random, origin, EnderscapeBlocks.CELESTIAL_GROWTH.defaultBlockState().setValue(StateProperties.FACING, state.getValue(StateProperties.FACING)), Mth.nextInt(random, 3, 10), 0, Mth.nextInt(random, 2, 4));
    }
}