package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.block.properties.DirectionSet;
import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MagniaSproutBlock extends DirectionalPlantBlock implements HasMagniaPolarity, SimpleWaterloggedBlock, EntityBlock {

    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final BooleanProperty OVERHEATED = StateProperties.OVERHEATED;
    public static final BooleanProperty WATERLOGGED = StateProperties.WATERLOGGED;

    protected final MagniaPolarity polarity;

    public MagniaSproutBlock(MagniaPolarity polarity, Properties settings) {
        super(DirectionSet.create().all(), settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(OVERHEATED, false).setValue(WATERLOGGED, false));
        this.polarity = polarity;
    }

    @Override
    protected MapCodec<MagniaSproutBlock> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                MagniaPolarity.CODEC.fieldOf("magnia_polarity").forGetter(sprout -> sprout.polarity),
                propertiesCodec()
        ).apply(instance, MagniaSproutBlock::new));
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagniaSproutBlockEntity(pos, state);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type1, BlockEntityType<E> type2, BlockEntityTicker<? super E> ticker) {
        return type2 == type1 ? (BlockEntityTicker<A>) ticker : null;
    }

    public static boolean isPowered(BlockState state) {
        return state.getBlock() instanceof MagniaSproutBlock && state.getValue(POWERED);
    }

    public static boolean isOverheated(BlockState state) {
        return state.getBlock() instanceof MagniaSproutBlock && state.getValue(OVERHEATED);
    }

    public static boolean shouldOverheat(Level world, BlockPos origin) {
        for (Direction dir : Direction.values()) {
            if (world.getBlockState(origin.relative(dir)).is(EnderscapeBlockTags.OVERHEATS_MAGNIA_SPROUTS)) return true;
        }
        return false;
    }

    public static boolean canPullEntities(BlockState state) {
        return isPowered(state) && !isOverheated(state);
    }

    private boolean trySetPowered(BlockState state, Level level, BlockPos pos) {
        if (shouldOverheat(level, pos) || !getNeighborSignal(state, level, pos, state.getValue(FACING))) return false;

        return setPowered(state, level, pos, true);
    }

    private boolean setPowered(BlockState state, Level level, BlockPos pos, boolean powered) {
        Vec3 vec = Vec3.atCenterOf(pos);
        level.setBlock(pos, state.setValue(POWERED, powered).setValue(OVERHEATED, !powered && isOverheated(state)), UPDATE_ALL);
        level.playSound(
                null,
                vec.x,
                vec.y,
                vec.z,
                powered ? polarity.getPowerOnSound() : polarity.getPowerOffSound(),
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
                level.playSound(null, vec.x, vec.y, vec.z, polarity.getOverheatSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
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
        return !level.isClientSide() && MagniaSproutBlock.canPullEntities(state) ? createTickerHelper(type, EnderscapeBlockEntities.MAGNIA_SPROUT.get(), MagniaSproutBlockEntity::tick) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, OVERHEATED, WATERLOGGED);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.isFaceSturdy(level, pos, facing);
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

        return state.setValue(POWERED, getNeighborSignal(state, level, pos, state.getValue(FACING)) && !shouldOverheat).setValue(OVERHEATED, shouldOverheat).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
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

    private boolean getNeighborSignal(BlockState state, SignalGetter level, BlockPos pos, Direction facing) {
        if (MagniaUtil.getStrongestPowerSignal(state, level, pos, facing.getOpposite()) > 0) return true;

        for (Direction direction : Direction.values()) {
            if (direction != facing && level.hasSignal(pos.relative(direction), direction)) return true;
        }

        if (level.hasSignal(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos abovePos = pos.above();

            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN && level.hasSignal(abovePos.relative(direction), direction)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void updateState(BlockState state, Level level, BlockPos pos, boolean schedule) {
        if (level.isClientSide()) return;

        boolean hasSignal = getNeighborSignal(state, level, pos, state.getValue(FACING));
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
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (!isPowered(state) && !isOverheated(state) && trySetPowered(state, level, pos)) level.scheduleTick(pos, this, 20);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos relative = pos.relative(getFacing(state));

        if (shouldBlisterMagnia(state, level, relative) && random.nextInt(3) == 0) {
            level.setBlockAndUpdate(relative, EnderscapeBlocks.BLISTERED_MAGNIA.get().defaultBlockState().setValue(StateProperties.OPTIONAL_MAGNIA_POLARITY, BlisteredMagniaBlock.selectPolarity(level, relative)));

            Vec3 center = relative.getCenter();
            level.sendParticles(EnderscapeParticles.MAGNIA_BLISTERING.get(), center.x(), center.y(), center.z(), 12, 0.7F, 0.7F, 0.7F, 0);
        }
    }

    private static boolean shouldBlisterMagnia(BlockState state, Level level, BlockPos relative) {
        return isPowered(state) && !level.getBlockState(relative).isAir() && level.getBlockState(relative).getBlock() instanceof MagniaBlock && MagniaUtil.isMatchingPolarity(state, level.getBlockState(relative));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos relative = pos.relative(getFacing(state));

        if (shouldBlisterMagnia(state, level, relative) && random.nextInt(2) == 0) {
            Direction direction = Direction.getRandom(random);
            if (direction.getAxis() != Direction.Axis.Y) {
                BlockPos side = relative.relative(direction);
                BlockState state2 = level.getBlockState(side);

                if (!state2.canOcclude() || !state2.isFaceSturdy(level, side, direction.getOpposite())) {
                    double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepX() * 0.6;
                    double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepY() * 0.6;
                    double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepZ() * 0.6;

                    level.addParticle(EnderscapeParticles.MAGNIA_BLISTERING.get(), (double) relative.getX() + d, (double) relative.getY() + e, (double) relative.getZ() + f, 0.0, 0.0, 0.0);
                }
            }
        }

        if (canPullEntities(state)) getPolarity(state).ifPresent(polarity -> {
            Vec3 start = Vec3.atCenterOf(pos);
            Vec3 end = Vec3.atCenterOf(MagniaSproutBlockEntity.getEndOfRange(level, state, pos, getFacing(state)));

            Vec3 position = polarity.getSproutParticlePosition().apply(start, end).add(
                    polarity.getSproutParticleOffset().sample(random),
                    polarity.getSproutParticleOffset().sample(random),
                    polarity.getSproutParticleOffset().sample(random)
            );

            Vec3 speed = polarity.getSproutParticleSpeed().apply(start, end);

            level.addParticle(polarity.getSproutParticleOptions(), position.x, position.y, position.z, speed.x, speed.y, speed.z);
        });
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.ofNullable(polarity);
    }
}