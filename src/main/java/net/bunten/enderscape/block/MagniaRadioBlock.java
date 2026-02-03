package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MagniaRadioBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<MagniaRadioBlock> CODEC = simpleCodec(MagniaRadioBlock::new);

    public static final BooleanProperty ENABLED = StateProperties.ENABLED;
    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final BooleanProperty IS_PLAYING = StateProperties.IS_PLAYING;
    public static final BooleanProperty WATERLOGGED = StateProperties.WATERLOGGED;

    public static final EnumProperty<Direction> FACING = StateProperties.HORIZONTAL_FACING;

    public static boolean isEnabled(BlockState updated) {
        return updated.getValue(ENABLED);
    }

    public static boolean isPowered(BlockState updated) {
        return updated.getValue(POWERED);
    }

    public static boolean isPlaying(BlockState updated) {
        return updated.getValue(IS_PLAYING);
    }

    public static boolean isAnotherSongPlayingNearby(Level level, BlockPos pos) {
        return BlockPos.findClosestMatch(pos, 36, 36, pos2 -> {
            if (pos2 == pos) return false;
            boolean radioPlaying = level.getBlockEntity(pos2) instanceof MagniaRadioBlockEntity entity && entity.getSongPlayer().isPlaying();
            boolean jukeboxPlaying = level.getBlockEntity(pos2) instanceof JukeboxBlockEntity entity && entity.getSongPlayer().isPlaying();
            return radioPlaying || jukeboxPlaying;
        }).isPresent();
    }

    public MagniaRadioBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ENABLED, false).setValue(POWERED, false).setValue(IS_PLAYING, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<MagniaRadioBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, POWERED, IS_PLAYING, WATERLOGGED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(ENABLED, true).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH, SOUTH -> box(0, 0, 3, 16, 12, 13);
            case EAST, WEST -> box(3, 0, 0, 13, 12, 16);
            default -> Shapes.empty();
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide()) {
            BlockState updated = state.cycle(ENABLED);

            if (!isEnabled(updated) && isPlaying(updated)) {
                MagniaRadioBlockEntity.tryStopPlaying(level, pos, player);
                updated = updated.cycle(IS_PLAYING);
            }

            level.setBlockAndUpdate(pos, updated);
            level.playSound(null, pos, isEnabled(updated) ? EnderscapeBlockSounds.MAGNIA_RADIO_POWER_ON : EnderscapeBlockSounds.MAGNIA_RADIO_POWER_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!isEnabled(updated)) {
                level.levelEvent(-624643, pos, 0);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return isEnabled(state) && !isPlaying(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide() && random.nextInt(4) == 0 && level.getBlockEntity(pos) instanceof MagniaRadioBlockEntity entity && !isAnotherSongPlayingNearby(level, pos)) {
            if (random.nextInt(8) == 0) {
                MagniaRadioBlockEntity.playAmbientSound(level, pos);
            } else {
                MagniaRadioBlockEntity.tryPlaySong(entity, level, pos);
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity mob, ItemStack stack) {
        super.setPlacedBy(level, pos, state, mob, stack);
        if (stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).contains(MagniaRadioBlockEntity.MAGNIA_RADIO_SONG_TAG_ID) && isEnabled(state)) {
            level.setBlock(pos, state.setValue(IS_PLAYING, true), 2);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bl) {
        if (!state.is(state2.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MagniaRadioBlockEntity entity) {
                entity.getSongPlayer().stop(level, state, false);
            }

            super.onRemove(state, level, pos, state2, bl);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl) {
        boolean hasSignal = level.hasNeighborSignal(pos);
        if (hasSignal != isPowered(state)) level.setBlock(pos, state.setValue(POWERED, hasSignal), 3);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagniaRadioBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return isPlaying(state) && isEnabled(state) ? createTickerHelper(type, EnderscapeBlockEntities.MAGNIA_RADIO.get(), MagniaRadioBlockEntity::tick) : null;
    }
}