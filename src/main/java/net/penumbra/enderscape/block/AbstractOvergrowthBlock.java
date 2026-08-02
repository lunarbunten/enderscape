package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.util.BlockUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public abstract class AbstractOvergrowthBlock extends DirectionalBlock implements BonemealableBlock {

    private static final LinkedHashMap<Direction, VoxelShape> PATH_VOXEL_SHAPES = BlockUtil.createRotatedShapes(0, 0, 0, 16, 15, 16);

    protected final boolean needsAir;
    @Nullable protected final Block pathBlock;
    protected final Block baseBlock;
    public final SoundEvent flattenSound;
    protected final boolean isPath;
    protected final DirectionSet properties;

    protected int bonemealRadius = 4;

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, @Nullable Block pathBlock, @Nullable SoundEvent flattenSound, boolean isPath, DirectionSet properties, Properties settings) {
        super(settings);

        this.needsAir = needsAir;
        this.baseBlock = baseBlock;
        this.pathBlock = pathBlock;
        this.flattenSound = flattenSound;
        this.isPath = isPath;
        this.properties = properties;

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, @Nullable Block pathBlock, @Nullable SoundEvent flattenSound, DirectionSet properties, Properties settings) {
        this(needsAir, baseBlock, pathBlock, flattenSound, false, properties, settings);
    }

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, DirectionSet properties, Properties settings) {
        this(needsAir, baseBlock, null, null, true, properties, settings);
    }

    public static Direction getDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();

        if (!properties.supports(direction)) direction = Direction.UP;

        BlockState state = defaultBlockState().setValue(FACING, direction);

        if (isPath && !hasAir(context.getLevel(), context.getClickedPos())) {
            return Block.pushEntitiesUp(state, baseBlock.defaultBlockState(), context.getLevel(), context.getClickedPos());
        }

        return state;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public BonemealableBlock.Type getType() {
        return Type.GROWER;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (needsAir && !hasAir(level, pos)) {
            BlockState state2 = pushEntitiesUp(state, baseBlock.defaultBlockState(), level, pos);
            level.setBlockAndUpdate(pos, state2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state2));
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player mob, InteractionHand hand, BlockHitResult result) {
        if (pathBlock != null && stack.getItem() instanceof ShovelItem && level.getBlockState(pos.relative(state.getValue(FACING))).isAir()) {
            level.playSound(mob, pos, flattenSound, SoundSource.BLOCKS, 1, 1);

            if (!level.isClientSide()) {
                level.setBlock(pos, pathBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)), Block.UPDATE_ALL);
                stack.hurtAndBreak(1, mob, hand.asEquipmentSlot());
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isPath && level.getBlockState(pos.relative(getDirection(state))).propagatesSkylightDown();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return !isPath;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, 2);
        BlockUtil.replaceSpherically(level, pos, state, bonemealRadius, state.getValue(FACING), (other) -> other.is(baseBlock));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!isPath) return super.getShape(state, level, pos, context);
        return PATH_VOXEL_SHAPES.get(state.getValue(FACING));
    }

    protected boolean hasAir(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction direction = state.getValue(FACING);

        if (isPath) {
            return BlockUtil.hasAirForPath(level, pos, direction);
        } else {
            return BlockUtil.hasAirAbove(level, pos, state.getValue(FACING));
        }
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return isPath;
    }

    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        if (direction == state.getValue(FACING) && !hasAir(level, pos) && isPath && needsAir) access.scheduleTick(pos, this, 1);
        return super.updateShape(state, level, access, pos, direction, pos2, state2, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState state2 = pushEntitiesUp(state, baseBlock.defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, state2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state2));
    }
}
