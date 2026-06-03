package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class VoidShaleBlock extends Block {

    public static final int MAX_ITERATIONS = 60;
    public static final int MAX_STRESS = 4;

    public static final BooleanProperty COOLDOWN = BooleanProperty.create("cooldown");
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final IntegerProperty ITERATION = IntegerProperty.create("iteration", 0, MAX_ITERATIONS);
    public static final IntegerProperty STRESS = IntegerProperty.create("stress", 0, MAX_STRESS);

    public VoidShaleBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(COOLDOWN, false).setValue(NATURAL, true).setValue(ITERATION, 0).setValue(STRESS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COOLDOWN, NATURAL, ITERATION, STRESS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(NATURAL, false);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getStress(state) == MAX_STRESS) {
            boolean shouldDestroy = true;

            for (Direction dir : Direction.values()) {
                BlockPos relative = pos.relative(dir);
                BlockState other = level.getBlockState(relative);
                int iteration = getIteration(state) + 1 + random.nextInt(2);

                if (canAttach(level, dir, other, relative)) {
                    shouldDestroy = false;
                    break;
                }

                if (other.is(this) && iteration < MAX_ITERATIONS) {
                    level.setBlock(relative, other.setValue(STRESS, MAX_STRESS).setValue(ITERATION, iteration), 2);
                    level.scheduleTick(relative, this, 1);
                }
            }

            if (shouldDestroy) {
                level.destroyBlock(pos, true);
            }

        } else if (onCooldown(state)) {
            level.setBlock(pos, state.cycle(COOLDOWN), 2);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity living && !living.hasEffect(MobEffects.SLOW_FALLING) && !onCooldown(state) && !isAttachedToBlock(level, pos)) {
            int stress = getStress(state);
            BlockState updated = state.setValue(STRESS, Math.min(MAX_STRESS, stress + 1)).setValue(COOLDOWN, true).setValue(ITERATION, 0);

            playShatterEffects(level, pos, updated, stress);
            level.setBlock(pos, updated, 2);
            level.scheduleTick(pos, this, living.isCrouching() ? 30 : 15);
        }
    }

    private void playShatterEffects(Level level, BlockPos pos, BlockState state, int stress) {
        SoundEvent sound = EnderscapeBlockSounds.VOID_SHALE_SHATTER_SOUNDS[Math.min(3, stress)];
        if (sound != SoundEvents.EMPTY) level.playSound(null, pos, sound, SoundSource.BLOCKS, 1, 1);

        if (level instanceof ServerLevel server) {
            Vec3 top = Vec3.atCenterOf(pos).add(0, 0.5125, 0);
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), top.x(), top.y(), top.z(), 6, 0.1F, 0.1F, 0.1F, 0);
        }
    }

    private boolean canAttach(Level level, Direction dir, BlockState state, BlockPos relative) {
        return state.isFaceSturdy(level, relative, dir) && !state.isAir() && !state.is(this);
    }

    private boolean isAttachedToBlock(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos relative = pos.relative(dir);
            BlockState other = level.getBlockState(relative);

            if (canAttach(level, dir, other, relative)) return true;
        }
        return false;
    }

    private int getStress(BlockState state) {
        return state.getValue(STRESS);
    }

    private boolean onCooldown(BlockState state) {
        return state.getValue(COOLDOWN);
    }

    private Integer getIteration(BlockState state) {
        return state.getValue(ITERATION);
    }
}