package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

public class MagniaBlock extends AbstractMagniaBlock implements HasMagniaPowerSignal {

    public static final IntegerProperty POWER = StateProperties.POWER;

    public MagniaBlock(MagniaPolarity polarity, Properties properties) {
        super(polarity, properties);
        registerDefaultState(stateDefinition.any().setValue(POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    public static int getPower(BlockState state) {
        return state.hasProperty(POWER) ? state.getValue(POWER) : 0;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getPower(state);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        return state.setValue(POWER, MagniaUtil.getStrongestPowerSignal(state, context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean bl) {
        if (MagniaUtil.getStrongestPowerSignal(state, level, pos) != getPower(state)) level.setBlockAndUpdate(pos, state.setValue(POWER, MagniaUtil.getStrongestPowerSignal(state, level, pos)));
    }

    @Override
    protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int i, int j) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Direction dir : Direction.values()) {
            mutable.setWithOffset(pos, dir);
            level.neighborShapeChanged(dir.getOpposite(), mutable, mutable.relative(dir.getOpposite()), state, i, j);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean bl) {
        if (!old.is(state.getBlock()) && !level.isClientSide()) {
            updateAllNeighbors(level, pos);
            updateNeighborsOfNeighboringMagnia(state, level, pos);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean bl) {
        if (!bl) {
            updateAllNeighbors(level, pos);
            updateNeighborsOfNeighboringMagnia(state, level, pos);
        }
    }

    private void updateNeighborsOfNeighboringMagnia(BlockState state, Level level, BlockPos pos) {
        if (getPolarity(state).isPresent()) return;

        for (Direction dir1 : Direction.values()) {
            BlockPos neighbor = pos.relative(dir1);

            if (MagniaUtil.isMatchingPolarity(state, level.getBlockState(neighbor))) {
                level.updateNeighborsAt(neighbor, this);

                for (Direction dir2 : Direction.values()) {
                    updateAllNeighbors(level, neighbor.relative(dir2));
                }
            }
        }
    }

    private void updateAllNeighbors(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) level.updateNeighborsAt(pos.relative(direction), this);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (getPower(state) == 0 && random.nextInt(800) == 0 && !BlockUtil.isBlockObstructed(level, pos)) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, polarity.getHumSound(), SoundSource.AMBIENT, 1, 1, false);
        }
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return MagniaUtil.isMatchingPolarity(state, neighbor) ? getPower(state) : 0;
    }
}