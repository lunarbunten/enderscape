package net.bunten.enderscape.blocks;

import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class MurushroomsBlock extends WallClingingBlock implements Fertilizable {
    public static final int MAX_AGE = 5;
    public static final IntProperty AGE = Properties.AGE_5;
    
    public MurushroomsBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH).with(AGE, 0));
    }

    public static boolean generate(BlockState state, WorldAccess world, BlockPos pos, Random random, int tries, int range) {
        int p = 0;
        for (int i = 0; i < tries; i++) {
            Direction dir = Direction.random(random);
            BlockPos pos2 = MathUtil.random(pos, random, range, range, range);

            if (world.isAir(pos2) && world.getBlockState(pos2.offset(dir)).isOpaque()) {
                if (dir.getAxis() != Direction.Axis.Y) {
                    world.setBlockState(pos2, state.with(FACING, dir.getOpposite()), NOTIFY_LISTENERS);
                    p++;
                }
            }
        }
        return p > 0;
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
        builder.add(FACING, AGE);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < MAX_AGE;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) < MAX_AGE;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var age = state.get(AGE);
        if (PlantUtil.generateMurushrooms(world, pos, random, age, 4, 4, 50)) {
            world.setBlockState(pos, state.cycle(AGE), NOTIFY_ALL);
        }
    }
}