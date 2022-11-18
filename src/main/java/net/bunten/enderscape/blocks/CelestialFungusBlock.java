package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CelestialFungusBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public static final IntegerProperty STAGE = StateProperties.STAGE;

    public CelestialFungusBlock(Properties settings) {
        super(DirectionProperties.create().up(), settings);
        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(EnderscapeBlocks.PLANTABLE_FUNGUS);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(4, 0, 4, 12, 9, 12, getFacing(state));
    }

    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.below()).is(EnderscapeBlocks.LARGE_CELESTIAL_FUNGUS_GROWABLE_ON);
    }

    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45;
    }

    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(STAGE) == 0) {
            world.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            PlantUtil.generateDefaultLargeCelestialFungus(world, random, pos);
        }
    }
}