package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractOvergrowthBlock extends DirectionalBlock implements BonemealableBlock {

    protected final boolean needsAir;
    @Nullable protected final Block pathBlock;
    protected final Block baseBlock;
    public final SoundEvent flattenSound;
    protected final boolean isPath;
    protected final DirectionProperties properties;

    protected int horizontalBonemealRange = 8;

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, @Nullable Block pathBlock, @Nullable SoundEvent flattenSound, boolean isPath, DirectionProperties properties, Properties settings) {
        super(settings);

        this.needsAir = needsAir;
        this.baseBlock = baseBlock;
        this.pathBlock = pathBlock;
        this.flattenSound = flattenSound;
        this.isPath = isPath;
        this.properties = properties;

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, @Nullable Block pathBlock, @Nullable SoundEvent flattenSound, DirectionProperties properties, Properties settings) {
        this(needsAir, baseBlock, pathBlock, flattenSound, false, properties, settings);
    }

    public AbstractOvergrowthBlock(boolean needsAir, Block baseBlock, DirectionProperties properties, Properties settings) {
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
        Direction dir = context.getNearestLookingDirection().getOpposite();

        if (!properties.supports(dir)) dir = Direction.UP;

        BlockState state = defaultBlockState().setValue(FACING, dir);

        if (isPath && !hasAir(state, context.getLevel(), context.getClickedPos())) {
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
        if (needsAir && !hasAir(state, level, pos)) {
            BlockState state2 = pushEntitiesUp(state, baseBlock.defaultBlockState(), level, pos);
            level.setBlockAndUpdate(pos, state2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state2));
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player mob, InteractionHand hand, BlockHitResult result) {
        if (pathBlock != null && stack.getItem() instanceof ShovelItem && world.getBlockState(pos.relative(state.getValue(FACING))).isAir()) {
            world.playSound(mob, pos, flattenSound, SoundSource.BLOCKS, 1, 1);
            if (!world.isClientSide()) {
                world.setBlock(pos, pathBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)), Block.UPDATE_ALL);
                stack.hurtAndBreak(1, mob, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return !isPath && world.getBlockState(pos.relative(getDirection(state))).propagatesSkylightDown(world, pos.relative(getDirection(state)));
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return !isPath;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int range = horizontalBonemealRange / 2;

        level.setBlock(pos, state, 2);
        Direction dir = state.getValue(FACING);

        for (int x = -range + 1; x < range; x++) {
            for (int y = -12; y < 12; y++) {
                for (int z = -range + 1; z < range; z++) {
                    BlockPos pos2 = pos.offset(x, y, z);
                    BlockPos relative = pos2.relative(dir);
                    boolean bl = level.getBlockState(relative).propagatesSkylightDown(level, relative) && level.getBlockState(pos2).is(baseBlock);
                    if (bl && Mth.sqrt(x * x + y * y + z * z) <= range) {
                        if (level.getRandom().nextFloat() < 0.6F) {
                            level.setBlock(pos2, state, 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (!isPath) return super.getShape(state, world, pos, context);
        return BlockUtil.createRotatedShape(0, 0, 0, 16, 15, 16, state.getValue(FACING));
    }

    public boolean hasAir(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos relative = pos.relative(facing);
        BlockState relativeState = level.getBlockState(relative);

        if (isPath) {
            return !relativeState.isSolid() || relativeState.getBlock() instanceof FenceGateBlock;
        } else {
            return LightEngine.getLightBlockInto(level, state, pos, relativeState, relative, facing, relativeState.getLightBlock(level, relative)) < 15;
        }
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState blockState) {
        return isPath;
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (direction == state.getValue(FACING) && !hasAir(state, world, pos) && isPath && needsAir) world.scheduleTick(pos, this, 1);
        return super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState state2 = pushEntitiesUp(state, baseBlock.defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, state2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state2));
    }
}
