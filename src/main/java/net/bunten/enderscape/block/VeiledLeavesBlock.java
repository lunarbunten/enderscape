package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.StateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.OptionalInt;

public class VeiledLeavesBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<VeiledLeavesBlock> CODEC = simpleCodec(VeiledLeavesBlock::new);

    public static final int DECAY_DISTANCE = 14;
    private static final int TICK_DELAY = 1;

    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, DECAY_DISTANCE);
    public static final BooleanProperty PERSISTENT = StateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = StateProperties.WATERLOGGED;

    @Override
    public MapCodec<? extends VeiledLeavesBlock> codec() {
        return CODEC;
    }

    public VeiledLeavesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(DISTANCE, DECAY_DISTANCE).setValue(PERSISTENT, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(DISTANCE) == DECAY_DISTANCE && !(Boolean)state.getValue(PERSISTENT);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (decaying(state)) {
            dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }
    }

    protected boolean decaying(BlockState state) {
        return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == DECAY_DISTANCE;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), 3);
    }

    @Override
    protected int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

        int i = getDistanceAt(state2) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) world.scheduleTick(pos, this, TICK_DELAY);

        return state;
    }

    public static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = DECAY_DISTANCE;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            mutable.setWithOffset(pos, direction);
            i = Math.min(i, getDistanceAt(level.getBlockState(mutable)) + 1);
            if (i == 1) {
                break;
            }
        }

        return state.setValue(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState state) {
        return getOptionalDistanceAt(state).orElse(DECAY_DISTANCE);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState state) {
        if (state.is(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        } else {
            return state.hasProperty(DISTANCE) ? OptionalInt.of(state.getValue(DISTANCE)) : OptionalInt.empty();
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.isRainingAt(pos.above())) {
            if (random.nextInt(15) == 1) {
                BlockPos pos2 = pos.below();
                BlockState state2 = level.getBlockState(pos2);
                if (!state2.canOcclude() || !state2.isFaceSturdy(level, pos2, Direction.UP)) {
                    ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.DRIPPING_WATER);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        return updateDistance(state, context.getLevel(), context.getClickedPos());
    }
}
