package net.enderscape.blocks;

import net.enderscape.util.EndMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class MurushroomsBlock extends WallClingingBlock {
    public MurushroomsBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public static void generate(BlockState state, WorldAccess world, BlockPos pos, Random random, int tries, int range) {
        for (int i = 0; i < tries; i++) {
            Direction dir = Direction.random(random);
            BlockPos randomPos = EndMath.random(pos, random, range, range, range);
            BlockPos posAround = randomPos.offset(dir);
            if (world.isAir(randomPos) && world.getBlockState(posAround).isOpaque()) {
                if (dir.getAxis() != Direction.Axis.Y) {
                    world.setBlockState(randomPos, (state).with(HorizontalFacingBlock.FACING, dir.getOpposite()), NOTIFY_LISTENERS);
                }
            }
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return switch (state.get(FACING)) {
            case EAST -> createCuboidShape(0, 2, 2, 4, 15, 14);
            case WEST -> createCuboidShape(12, 2, 2, 16, 15, 14);
            case SOUTH -> createCuboidShape(2, 2, 0, 14, 15, 4);
            default -> createCuboidShape(2, 2, 12, 14, 15, 16);
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(200) == 0) {
            MurushroomsBlock.generate(getDefaultState(), world, pos, random, 25, 4);
        }
    }
}