package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CelestialFungusBlock extends PlantBlock implements Fertilizable, LayerMapped {
    public static final IntProperty STAGE = Properties.STAGE;

    public CelestialFungusBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(STAGE, 0));
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EnderscapeBlocks.FUNGUS_PLANTABLE_ON);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return createCuboidShape(4, 0, 4, 12, 9, 12);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.down()).isIn(EnderscapeBlocks.LARGE_CELESTIAL_FUNGUS_GROWABLE_ON);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycle(STAGE), 4);
        } else {
            PlantUtil.generateDefaultLargeCelestialFungus(world, random, pos);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}