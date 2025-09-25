package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeGameEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DriftJellyBlock extends HalfTransparentBlock {
    public DriftJellyBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, 14, 16);
    }

    protected Vec3 getBounceVelocity(Entity entity) {
        Vec3 vel = entity.getDeltaMovement();

        double x = vel.x();
        double z = vel.z();

        x = Mth.clamp(x * 2, -23, 23);
        z = Mth.clamp(z * 2, -23, 23);

        double height = 1.25;
        if (entity instanceof LivingEntity mob && mob.isFallFlying()) height += 0.2;
        return new Vec3(x, height, z);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce() || entity.getDeltaMovement().y() > -0.1) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            entity.setDeltaMovement(getBounceVelocity(entity));
            entity.gameEvent(EnderscapeGameEvents.BOUNCE);
        }
    }

    private void playBounceEffects(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            level.playSound(null, pos, EnderscapeBlockSounds.DRIFT_JELLY_BOUNCE, SoundSource.BLOCKS, 1, 1.2F);
        }

        if (level.isClientSide()) {
            Vec3 vec3 = pos.getCenter().add(0, 0.75, 0);
            BlockParticleOption option = new BlockParticleOption(ParticleTypes.DUST_PILLAR, level.getBlockState(pos));

            for (int i = 0; i < 20; i++) {
                double x = vec3.x;
                double y = vec3.y;
                double z = vec3.z;
                double xd = level.getRandom().nextGaussian() * 0.05F;
                double yd = level.getRandom().nextGaussian() * 0.05F;
                double zd = level.getRandom().nextGaussian() * 0.05F;

                level.addParticle(option, x, y, z, xd, yd, zd);
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0, level.damageSources().fall());
            playBounceEffects(level, pos);
        }
    }
}