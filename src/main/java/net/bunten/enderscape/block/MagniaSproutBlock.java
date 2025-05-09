package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.block.properties.MagniaType;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.bunten.enderscape.util.BlockUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.bunten.enderscape.block.properties.MagniaType.REPULSIVE;

public abstract class MagniaSproutBlock extends DirectionalPlantBlock implements SimpleWaterloggedBlock, EntityBlock {

    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final BooleanProperty OVERHEATED = StateProperties.OVERHEATED;
    public static final BooleanProperty WATERLOGGED = StateProperties.WATERLOGGED;

    public final MagniaType magniaType;

    public MagniaSproutBlock(MagniaType magniaType, Properties settings) {
        super(DirectionProperties.create().all(), settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(OVERHEATED, false).setValue(WATERLOGGED, false));
        this.magniaType = magniaType;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagniaSproutBlockEntity(pos, state);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type1, BlockEntityType<E> type2, BlockEntityTicker<? super E> ticker) {
        return type2 == type1 ? (BlockEntityTicker<A>) ticker : null;
    }

    public static MagniaType getMagniaType(BlockState state) {
        return state.getBlock() instanceof MagniaSproutBlock sprout ? sprout.magniaType : null;
    }

    public static boolean isPowered(BlockState state) {
        return state.getBlock() instanceof MagniaSproutBlock && state.getValue(POWERED);
    }

    public static boolean isOverheated(BlockState state) {
        return state.getBlock() instanceof MagniaSproutBlock && state.getValue(OVERHEATED);
    }

    public static boolean shouldOverheat(Level world, BlockPos origin) {
        for (Direction dir : Direction.values()) {
            var pos = origin.relative(dir);
            if (world.getBlockState(pos).is(EnderscapeBlockTags.OVERHEATS_MAGNIA_SPROUTS)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canPullEntities(BlockState state) {
        return isPowered(state) && !isOverheated(state);
    }

    public static boolean isRepulsive(BlockState state) {
        return getMagniaType(state) == REPULSIVE;
    }

    private boolean trySetPowered(BlockState state, Level level, BlockPos pos) {
        if (isOverheated(state) || shouldOverheat(level, pos) || !getNeighborSignal(level, pos, state.getValue(FACING))) return false;

        return setPowered(state, level, pos, true);
    }

    private boolean setPowered(BlockState state, Level level, BlockPos pos, boolean powered) {
        Vec3 vec = Vec3.atCenterOf(pos);
        level.setBlock(pos, state.setValue(POWERED, powered), UPDATE_ALL);
        level.playSound(
                null,
                vec.x,
                vec.y,
                vec.z,
                powered ? magniaType.getPowerOnSound() : magniaType.getPowerOffSound(),
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );
        level.gameEvent(null, powered ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        return true;
    }

    private void setOverheated(BlockState state, Level level, BlockPos pos, boolean value) {
        if (value) {
            Vec3 vec = Vec3.atCenterOf(pos);
            BlockState updatedState = state;

            if (isPowered(state)) {
                updatedState = updatedState.cycle(POWERED);
                level.playSound(null, vec.x, vec.y, vec.z, magniaType.getOverheatSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            level.setBlock(pos, updatedState.setValue(OVERHEATED, value), UPDATE_ALL);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        } else {
            level.setBlock(pos, state.setValue(OVERHEATED, value), UPDATE_ALL);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() ? createTickerHelper(type, EnderscapeBlockEntities.MAGNIA_SPROUT, MagniaSproutBlockEntity::tick) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, OVERHEATED, WATERLOGGED);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.isFaceSturdy(level, pos, facing) && floor.isSolidRender(level, pos.relative(facing.getOpposite()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BlockUtil.createRotatedShape(3, 0, 3, 13, 13, 13, state.getValue(FACING));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        FluidState fluid = level.getFluidState(pos);
        boolean shouldOverheat = shouldOverheat(level, pos);

        return state.setValue(POWERED, getNeighborSignal(level, pos, state.getValue(FACING)) && !shouldOverheat).setValue(OVERHEATED, shouldOverheat).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
        return super.getFluidState(state);
    }

    private boolean getNeighborSignal(SignalGetter getter, BlockPos pos, Direction facing) {
        for (Direction direction : Direction.values()) {
            if (direction != facing && getter.hasSignal(pos.relative(direction), direction)) return true;
        }

        if (getter.hasSignal(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos abovePos = pos.above();

            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN && getter.hasSignal(abovePos.relative(direction), direction)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void updateState(BlockState state, Level level, BlockPos pos, boolean schedule) {
        if (level.isClientSide()) return;

        boolean hasSignal = getNeighborSignal(level, pos, state.getValue(FACING));
        boolean shouldOverheat = shouldOverheat(level, pos);
        boolean overheated = isOverheated(state);
        boolean powered = isPowered(state);

        if (overheated != shouldOverheat) {
            setOverheated(state, level, pos, shouldOverheat);
        }

        if (powered != hasSignal) {
            if (hasSignal) {
                trySetPowered(state, level, pos);
            } else {
                if (schedule) {
                    level.scheduleTick(pos, this, 5);
                } else {
                    setPowered(state, level, pos, false);
                }
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl) {
        updateState(state, level, pos, true);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updateState(state, level, pos, false);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!isPowered(state) && player.getItemInHand(InteractionHand.MAIN_HAND).is(EnderscapeItemTags.POWERS_MAGNIA_WHEN_MINED_WITH)) {
            if (trySetPowered(state, level, pos)) level.scheduleTick(pos, this, 30);
        }

        super.attack(state, level, pos, player);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (!isPowered(state) && !isOverheated(state) && trySetPowered(state, level, pos)) level.scheduleTick(pos, this, 20);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (canPullEntities(state)) {
            Vec3 center = Vec3.atCenterOf(pos);
            Vec3 midair = Vec3.atCenterOf(pos.relative(getFacing(state), 6));
            Vec3 origin = midair;

            double xs = (center.x() - midair.x()) * 0.2F;
            double ys = (center.y() - midair.y()) * 0.2F;
            double zs = (center.z() - midair.z()) * 0.2F;

            if (isRepulsive(state)) {
                xs = -xs;
                ys = -ys;
                zs = -zs;
                origin = center;
            }

            level.addParticle(magniaType.getParticle(), origin.x() + (random.nextGaussian() / 4), origin.y() + (random.nextGaussian() / 3), origin.z() + (random.nextGaussian() / 4), xs, ys, zs);
        }
    }
}