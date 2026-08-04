package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

import static net.penumbra.enderscape.manager.VoidManager.*;

public class VoidFireBlock extends BaseFireBlock {
    public static final MapCodec<VoidFireBlock> CODEC = simpleCodec(VoidFireBlock::new);

    @Override
    public MapCodec<VoidFireBlock> codec() {
        return CODEC;
    }

    public VoidFireBlock(final BlockBehaviour.Properties properties) {
        super(properties, 1.0F);
    }

    @Override
    protected BlockState updateShape(
            final BlockState state,
            final LevelReader level,
            final ScheduledTickAccess ticks,
            final BlockPos pos,
            final Direction directionToNeighbour,
            final BlockPos neighbourPos,
            final BlockState neighbourState,
            final RandomSource random
    ) {
        return canSurvive(state, level, pos) ? defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return canSurviveOnBlock(below) && below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    public static boolean canSurviveOnBlock(final BlockState state) {
        return state.is(EnderscapeBlockTags.VOID_FIRE_BASE_BLOCKS);
    }

    @Override
    protected boolean canBurn(final BlockState state) {
        return false;
    }

    @Override
    protected void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);

        if (level instanceof ServerLevel server) {
            modifyVoidTicks(entity, value -> Math.max(value, MAXIMUM_VOID_TICKS));
            modifyVoidTickDownDelay(entity, value -> Math.max(value, MAXIMUM_VOID_TICKS));

            if (entity instanceof LivingEntity) {
                entity.hurtServer(server, level.damageSources().source(EnderscapeDamageTypes.IN_VOID_FIRE), 1.0F);
            }
        }
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        if (random.nextInt(24) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    EnderscapeBlockSounds.VOID_FIRE_AMBIENT,
                    SoundSource.BLOCKS,
                    Mth.nextFloat(random, 0.15F, 0.65F),
                    Mth.nextFloat(random, 0.8F, 1.2F),
                    false
            );
        }

        for (int i = 0; i < 3; i++) {
            double xx = pos.getX() + random.nextDouble();
            double yy = pos.getY() + Mth.nextDouble(random, -0.15, 0.15) + 0.5;
            double zz = pos.getZ() + random.nextDouble();

            level.addParticle(EnderscapeParticles.VOID_STARS, xx, yy, zz, 0.0, Mth.nextDouble(random, 0.04, 0.06), 0.0);
        }
    }
}