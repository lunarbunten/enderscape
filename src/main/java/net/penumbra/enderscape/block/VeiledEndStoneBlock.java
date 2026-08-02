package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

public class VeiledEndStoneBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<VeiledEndStoneBlock> CODEC = simpleCodec(VeiledEndStoneBlock::new);

    private static final UniformInt MAXIMUM_VEGETATION_GENERATED = UniformInt.of(2, 4);
    private static final UniformInt BONE_MEAL_VEGETATION_RADIUS = UniformInt.of(2, 4);

    public VeiledEndStoneBlock(Properties settings) {
        super(true, Blocks.END_STONE, null, null, false, DirectionSet.create().up(), settings);
    }

    @Override
    protected MapCodec<VeiledEndStoneBlock> codec() {
        return CODEC;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        generateVegetation(level, random, origin);
    }

    public static void generateVegetation(WorldGenLevel level, RandomSource random, BlockPos origin) {
        int generated = 0;

        int maximum = MAXIMUM_VEGETATION_GENERATED.sample(random);
        int radius = BONE_MEAL_VEGETATION_RADIUS.sample(random);

        for (BlockPos pos : BlockPos.randomInCube(random, 15, origin, radius)) {
            if (generated >= maximum) break;

            BlockState plant = EnderscapeBlocks.VEILED_OVERGROWTH_BONEMEAL_PROVIDER.getState(level, random, pos);

            if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).is(EnderscapeBlocks.VEILED_END_STONE)) {
                level.setBlock(pos, plant, 2);
                generated++;
            }
        }
    }
}