package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.EnderscapeProperties;
import net.bunten.enderscape.blocks.properties.GrowthPart;
import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.util.States;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CelestialGrowthBlock extends PlantBlock implements LayerMapped, Fertilizable {
    public static final EnumProperty<GrowthPart> GROWTH_PART = EnderscapeProperties.GROWTH_PART;

    public CelestialGrowthBlock(Settings settings) {
        super(settings);
        setDefaultState(getPartState(GrowthPart.TOP));
    }

    private BlockState getPartState(GrowthPart value) {
        return getDefaultState().with(GROWTH_PART, value);
    }

    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(GROWTH_PART);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(GROWTH_PART) == GrowthPart.TOP) {
            return createCuboidShape(2, 0, 2, 14, 13, 14);
        } else {
            return createCuboidShape(2, 0, 2, 14, 16, 14);
        }
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EnderscapeBlocks.GROWTH_PLANTABLE_OM) || floor.isOf(this);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.canPlaceAt(world, pos)) {
            if (world.getBlockState(pos.down()).isOf(this)) {
                return getPartState(world.getBlockState(pos.up()).isAir() ? GrowthPart.TOP : GrowthPart.MIDDLE);
            } else {
                return getPartState(world.getBlockState(pos.up()).isOf(this) ? GrowthPart.BOTTOM : GrowthPart.TOP);
            }
        } else {
            return States.AIR;
        }
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos.up()).isAir()) {
            world.setBlockState(pos.up(), state.with(GROWTH_PART, GrowthPart.TOP));
            world.setBlockState(pos, state.with(GROWTH_PART, GrowthPart.MIDDLE));
        } else {
            PlantUtil.generateCelestialGrowth(world, random, pos, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, 6, 4, 4, 6);
        }
    }
}