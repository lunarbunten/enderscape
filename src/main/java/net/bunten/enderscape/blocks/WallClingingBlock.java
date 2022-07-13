package net.bunten.enderscape.blocks;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.util.States;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WallClingingBlock extends HorizontalFacingBlock {
    protected WallClingingBlock(Settings settings) {
        super(settings);
    }

    public static Direction getDirection(BlockState state) {
        return state.get(FACING);
    }

    public static boolean canPlaceAt(WorldView world, BlockPos pos, Direction dir) {
        BlockPos offpos = pos.offset(dir);
        return world.getBlockState(offpos).isSideSolidFullSquare(world, offpos, dir.getOpposite());
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlaceAt(world, pos, getDirection(state).getOpposite());
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction dir : ctx.getPlacementDirections()) {
            BlockState state;
            if (dir.getAxis() == Direction.Axis.Y) {
                return null;
            } else {
                state = getDefaultState().with(FACING, dir.getOpposite());
            }

            if (state.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return state;
            }
        }

        return null;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return getDirection(state).getOpposite() == dir && !state.canPlaceAt(world, pos) ? States.AIR : super.getStateForNeighborUpdate(state, dir, newState, world, pos, posFrom);
    }
}