package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.*;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class EndGrowthBlock extends PlantBlock implements LayerMapped {
    public static final EnumProperty<Part> PART = EndProperties.PART;

    public EndGrowthBlock(Settings settings) {
        super(settings);
        setDefaultState(getPartState(Part.TOP));
    }

    private BlockState getBlockState(WorldView world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    private BlockState getPartState(Part value) {
        return getDefaultState().with(PART, value);
    }

    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(PART) == Part.TOP) {
            return createCuboidShape(2, 0, 2, 14, 13, 14);
        } else {
            return createCuboidShape(2, 0, 2, 14, 16, 14);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.canPlaceAt(world, pos)) {
            if (getBlockState(world, pos.down()).isOf(this)) {
                return getPartState(getBlockState(world, pos.up()).isAir() ? Part.TOP : Part.MIDDLE);
            } else {
                return getPartState(getBlockState(world, pos.up()).isOf(this) ? Part.BOTTOM : Part.TOP);
            }
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EndBlocks.GROWTH_PLANTABLE_BLOCKS) || floor.isOf(this);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}