package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.States;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BulbFlowerBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public BulbFlowerBlock(Properties settings) {
        super(DirectionProperties.create().up(), settings);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(EnderscapeBlocks.PLANTABLE_BULB_FLOWER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(2, 0, 2, 14, 15, 14, getFacing(state));
    }

    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos origin, BlockState state) {
        int i = 0;
        int max = MathUtil.nextInt(random, 1, 3);
        var range = MathUtil.nextInt(random, 4, 8);

        for (BlockPos pos : BlockPos.randomInCube(random, 30, origin, range)) {
            if (i >= max) break;
            BlockState plant = States.BULB_FLOWER_BONEMEAL_PROVIDER.getState(random, pos);
            if (world.isEmptyBlock(pos) && plant.canSurvive(world, pos)) {
                world.setBlock(pos, plant, 2);
                i++;
            }
        }
    }
}