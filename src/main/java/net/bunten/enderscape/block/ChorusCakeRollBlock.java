package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChorusCakeRollBlock extends Block {
    public static final MapCodec<ChorusCakeRollBlock> CODEC = simpleCodec(ChorusCakeRollBlock::new);

    public static final IntegerProperty BITES = StateProperties.BITES;
    public static final EnumProperty<Direction> FACING = StateProperties.HORIZONTAL_FACING;

    @Override
    public MapCodec<ChorusCakeRollBlock> codec() {
        return CODEC;
    }

    public ChorusCakeRollBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(BITES, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int shorten = state.getValue(BITES) * 2;
        return switch (state.getValue(FACING)) {
            case NORTH -> box(4, 0, 1 + shorten, 12, 7, 15);
            case WEST -> box(1 + shorten, 0, 4, 15, 7, 12);
            case EAST -> box(1, 0, 4, 15 - shorten, 7, 12);
            case SOUTH -> box(4, 0, 1, 12, 7, 15 - shorten);
            default -> Shapes.empty();
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.isClientSide()) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eat(level, pos, state, player);
    }

    protected static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (player.canEat(false)) {
            if (level.isClientSide()) makeEatingParticles(level, state, player);

            player.getFoodData().eat(2, 0.1F);
            player.playSound(EnderscapeBlockSounds.CHORUS_CAKE_ROLL_EAT, 1, 1);
            player.awardStat(Stats.EAT_CAKE_SLICE);

            level.gameEvent(player, GameEvent.EAT, pos);

            int bites = state.getValue(BITES);

            if (bites < 6) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                if (level instanceof ServerLevel server) teleportEntityRandomly(server, player);
                level.destroyBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static void makeEatingParticles(LevelAccessor level, BlockState state, Player player) {
        for (int i = 0; i < 8; i++) {
            RandomSource random = level.getRandom();
            double offsetY = -random.nextFloat() * 0.6 - 0.3;

            Vec3 position = new Vec3((random.nextFloat() - 0.5) * 0.3, offsetY, 0.6).xRot(-player.getXRot() * (float) (Math.PI / 180.0)).yRot(-player.getYRot() * (float) (Math.PI / 180.0)).add(player.getX(), player.getEyeY(), player.getZ());
            Vec3 velocity = new Vec3((random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).xRot(-player.getXRot() * (float) (Math.PI / 180.0)).yRot(-player.getYRot() * (float) (Math.PI / 180.0));

            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), position.x, position.y, position.z, velocity.x, velocity.y + 0.05, velocity.z);
        }
    }

    private static boolean teleportEntityRandomly(ServerLevel level, LivingEntity entity) {
        for (int i = 0; i < 16; i++) {
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16;
            double y = Mth.clamp(entity.getY() + (entity.getRandom().nextDouble() - 0.5) * 16, level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1);
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16;

            if (entity.isPassenger()) entity.stopRiding();
            Vec3 previousPosition = entity.position();

            if (entity.randomTeleport(x, y, z, true)) {
                level.gameEvent(GameEvent.TELEPORT, previousPosition, GameEvent.Context.of(entity));
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, entity instanceof Player ? SoundSource.PLAYERS : SoundSource.NEUTRAL);
                entity.resetFallDistance();

                if (entity instanceof Player player) player.resetCurrentImpulseContext();
                return true;
            }
        }

        return false;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, level, pos, pos2);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(BITES, FACING);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getOutputSignal(state.getValue(BITES));
    }

    public static int getOutputSignal(int i) {
        return (7 - i) * 2;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
