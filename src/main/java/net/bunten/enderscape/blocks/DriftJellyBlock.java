package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DriftJellyBlock extends HalfTransparentBlock implements HasRenderType {
    public DriftJellyBlock(Properties settings) {
        super(settings);
    }

    protected boolean allowsBouncing(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos.above()).isAir() ? true : false;
    }


    protected double getBounceHeight(LivingEntity mob) {
        double height = 1.2;
        if (mob.isFallFlying()) height += 0.2;
        return height;
    }

    protected Vec3 getBounceVelocity(LivingEntity mob) {
        Vec3 vel = mob.getDeltaMovement();

        double x = vel.x();
        double z = vel.z();

        x = MathUtil.clamp(x * 2, -23, 23);
        z = MathUtil.clamp(z * 2, -23, 23);

        return new Vec3(x, getBounceHeight(mob), z);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity mob && !mob.isSuppressingBounce() && allowsBouncing(world, pos)) {
            world.playSound(null, pos, EnderscapeSounds.DRIFT_JELLY_BOUNCE, SoundSource.BLOCKS, 1, 1.2F);
            entity.setDeltaMovement(getBounceVelocity(mob));
        }
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!entity.isSuppressingBounce()) {
            entity.causeFallDamage(fallDistance, 0, DamageSource.FALL);
            return;
        }

        super.fallOn(world, state, pos, entity, fallDistance);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.translucent();
    }
}