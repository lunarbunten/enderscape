package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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

public class MurushroomsBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = StateProperties.AGE_5;
    
    public MurushroomsBlock(Properties settings) {
        super(DirectionProperties.create().all(), settings);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isFaceSturdy(world, pos, getFacing(state));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(1, 0, 1, 15, 6, 15, state.getValue(FACING));
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.7;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) < MAX_AGE) {
            world.setBlock(pos, state.cycle(AGE), 4);
        } else {
            if (getFacing(state).getAxis() == Axis.Y) {
                PlantUtil.generateDefaultLargeMurushroom(getFacing(state), world, random, pos);
            } else {
                PlantUtil.generateMurushrooms(world, pos, random, new MurushroomFeatureConfig(8, 8, MAX_AGE, 100));
            }
        }
    }
}