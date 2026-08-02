package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds.*;

public class VoidShaleBlock extends Block {

    private static final Predicate<LivingEntity> ENTITY_CAN_SHATTER = (entity) -> !entity.is(EnderscapeEntityTags.VOID_SHALE_WALKABLE_MOBS) && !entity.hasEffect(MobEffects.SLOW_FALLING);

    private static final Function<LivingEntity, Integer> ENTITY_IDLE_SHATTER_INTERVAL = (entity) -> entity.isCrouching() ? 24 : 12;
    private static final BiPredicate<LivingEntity, Integer> ENTITY_IS_IDLING = (entity, ticks) -> {
        int interval = ENTITY_IDLE_SHATTER_INTERVAL.apply(entity);
        return ticks >= interval && ticks % interval == 0;
    };

    private static final int MAX_ITERATIONS = StateProperties.MAX_VOID_SHALE_ITERATIONS;
    private static final int MAX_STRESS = StateProperties.MAX_VOID_SHALE_STRESS;

    private static final BooleanProperty NATURAL = StateProperties.NATURAL;
    private static final IntegerProperty ITERATION = StateProperties.VOID_SHALE_ITERATION;
    private static final IntegerProperty STRESS = StateProperties.VOID_SHALE_STRESS;

    public static final SoundEvent[] SHATTER_SOUNDS = {
            VOID_SHALE_SHATTER_LOW,
            VOID_SHALE_SHATTER_MEDIUM,
            VOID_SHALE_SHATTER_HIGH
    };

    public VoidShaleBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(NATURAL, true).setValue(ITERATION, 0).setValue(STRESS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NATURAL, ITERATION, STRESS);
    }

    private static int getStress(BlockState state) {
        return state.getValue(STRESS);
    }

    private static int getIteration(BlockState state) {
        return state.getValue(ITERATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(NATURAL, false);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        triggerBreakReaction(level, state, pos);
    }

    public static void onEntityIdle(ServerLevel level, LivingEntity entity, int idleTicks) {
        BlockPos pos = entity.getOnPos();

        if (ENTITY_CAN_SHATTER.test(entity) && ENTITY_IS_IDLING.test(entity, idleTicks) && !attachedToBlock(level, pos)) {
            BlockState state = entity.getBlockStateOn();

            if (getStress(state) == MAX_STRESS) {
                triggerBreakReaction(level, state, pos);
            } else {
                progressShattering(level, state, pos);
            }
        }
    }

    private static void progressShattering(ServerLevel level, BlockState state, BlockPos pos) {
        level.playSound(null, pos, SHATTER_SOUNDS[getStress(state)], SoundSource.BLOCKS, 1, 1);

        Vec3 top = pos.getCenter().add(0, 0.5125, 0);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), top.x(), top.y(), top.z(), 6, 0.1F, 0.1F, 0.1F, 0);

        BlockState updated = state.cycle(STRESS).setValue(ITERATION, 0);

        level.setBlock(pos, updated, 2);
    }

    private static void triggerBreakReaction(ServerLevel level, BlockState state, BlockPos pos) {
        boolean shouldDestroy = true;

        for (Direction direction : Direction.values()) {
            BlockPos relative = pos.relative(direction);
            BlockState other = level.getBlockState(relative);
            int iteration = getIteration(state) + 1 + level.getRandom().nextInt(2);

            if (canAttach(level, direction.getOpposite(), state, other, relative)) {
                shouldDestroy = false;
                break;
            }

            if (other.is(state.getBlock()) && iteration < MAX_ITERATIONS) {
                if (!attachedToBlock(level, relative)) {
                    level.setBlock(relative, other.setValue(STRESS, MAX_STRESS).setValue(ITERATION, iteration), 2);
                    level.scheduleTick(relative, state.getBlock(), 1);
                }
            }
        }

        if (shouldDestroy) {
            level.destroyBlock(pos, true);
        }
    }

    private static boolean attachedToBlock(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos relative = pos.relative(direction);
            BlockState other = level.getBlockState(relative);

            if (canAttach(level, direction.getOpposite(), level.getBlockState(pos), other, relative)) return true;
        }
        return false;
    }

    private static boolean canAttach(Level level, Direction direction, BlockState state, BlockState other, BlockPos relative) {
        return (other.isFaceSturdy(level, relative, direction) || other.getBlock().equals(EnderscapeBlocks.VOID_LACHRYMA)) && !other.isAir() && !other.is(state.getBlock());
    }
}