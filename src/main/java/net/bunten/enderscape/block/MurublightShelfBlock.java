package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionSet;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MurublightShelfBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = StateProperties.AGE_5;
    public static final MapCodec<MurublightShelfBlock> CODEC = simpleCodec(MurublightShelfBlock::new);
    
    public MurublightShelfBlock(Properties settings) {
        super(DirectionSet.create().horizontal(), settings);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    public MapCodec<MurublightShelfBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.isFaceSturdy(level, pos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(1, 0, 1, 15, 6, 15, state.getValue(FACING));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.7;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos origin, BlockState state) {
        if (state.getValue(AGE) < MAX_AGE) {
            world.setBlock(origin, state.cycle(AGE), 4);
        } else {
            int max = Mth.nextInt(random, 2, 4);

            int i = 0;
            for (BlockPos pos : BlockPos.randomInCube(random, 30, origin, Mth.nextInt(random, 3, 10))) {
                if (i >= max) break;
                if (!world.getBlockState(pos).is(this) && generate(world, origin)) i++;
            }
        }
    }

    private static BlockState stateForGeneration(Direction direction) {
        return EnderscapeBlocks.MURUBLIGHT_BRACKET.get().defaultBlockState().setValue(MurublightShelfBlock.FACING, direction).setValue(MurublightShelfBlock.AGE, MurublightShelfBlock.MAX_AGE);
    }

    public static boolean canGenerate(LevelAccessor world, BlockPos pos, Direction direction) {
        if (!world.getBlockState(pos).isAir() || direction.getAxis() == Direction.Axis.Y) return false;
        BlockPos relative = pos.relative(direction.getOpposite());
        return world.getBlockState(relative).isFaceSturdy(world, relative, direction);
    }

    public static boolean generate(LevelAccessor world, BlockPos pos) {
        if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above()) && world.isEmptyBlock(pos.below())) {
            for (Direction direction : Direction.values()) {
                if (direction.getAxis() == Direction.Axis.Y) continue;
                BlockPos relative = pos.relative(direction.getOpposite());
                if (world.getBlockState(relative).isFaceSturdy(world, relative, direction)) {
                    world.setBlock(pos, stateForGeneration(direction), 2);
                    return true;
                }
            }
        }

        return false;
    }
}