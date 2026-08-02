package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;

public class CelestialOvergrowthBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CelestialOvergrowthBlock> CODEC = simpleCodec(CelestialOvergrowthBlock::new);

    private static final UniformInt MAXIMUM_VEGETATION_GENERATED = UniformInt.of(1, 3);
    private static final UniformInt BONE_MEAL_VEGETATION_RADIUS = UniformInt.of(2, 4);

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

        int generated = 0;

        int maximum = MAXIMUM_VEGETATION_GENERATED.sample(random);
        int radius = BONE_MEAL_VEGETATION_RADIUS.sample(random);

        for (BlockPos pos : BlockPos.randomInCube(random, 15, origin, radius)) {
            if (generated >= maximum) break;

            BlockState plant = EnderscapeBlocks.CELESTIAL_OVERGROWTH_BONEMEAL_PROVIDER.getState(level, random, pos);

            if (level.isEmptyBlock(pos) && plant.canSurvive(level, pos)) {
                level.setBlock(pos, plant, 2);
                generated++;
            }
        }

        AbstractGrowthBlock.generatePatch(level, random, origin, EnderscapeBlocks.CELESTIAL_GROWTH.defaultBlockState().setValue(StateProperties.FACING, state.getValue(StateProperties.FACING)), Mth.nextInt(random, 3, 10), 0, Mth.nextInt(random, 2, 4));
    }
}