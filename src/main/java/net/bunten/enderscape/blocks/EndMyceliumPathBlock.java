package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EndMyceliumPathBlock extends AbstractMyceliumBlock {
    public EndMyceliumPathBlock(DirectionProperties properties, Properties settings) {
        super(properties, settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST -> box(1, 0, 0, 16, 16, 16);
            case EAST -> box(0, 0, 0, 15, 16, 16);
            case NORTH -> box(0, 0, 1, 16, 16, 16);
            case SOUTH -> box(0, 0, 0, 16, 16, 15);
            case UP -> box(0, 0, 0, 16, 15, 16);
            default -> box(0, 1, 0, 16, 16, 16);
        };
    }
    
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.relative(state.getValue(FACING)));
        return !blockState.getMaterial().isSolid() || blockState.getBlock() instanceof FenceGateBlock;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}