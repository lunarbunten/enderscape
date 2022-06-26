package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DriftJellyBlock extends TransparentBlock implements LayerMapped {
    public DriftJellyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0, DamageSource.FALL);
        }
    }

    protected double getBounceHeight(LivingEntity mob) {
        double height = 1.2;
        return mob.isFallFlying() ? height += 0.2 : height;
    }

    protected Vec3d getBounceVelocity(LivingEntity mob) {
        Vec3d vel = mob.getVelocity();

        double x = vel.getX();
        double z = vel.getZ();

        x = MathUtil.clamp(x * 2, -23, 23);
        z = MathUtil.clamp(z * 2, -23, 23);

        return new Vec3d(x, getBounceHeight(mob), z);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity mob && !mob.bypassesLandingEffects() && allowsBouncing(world, pos)) {
            world.playSound(null, pos, EnderscapeSounds.BLOCK_DRIFT_JELLY_BOUNCE, SoundCategory.BLOCKS, 1, 1.2F);
            entity.setVelocity(getBounceVelocity(mob));
        }
    }

    protected boolean allowsBouncing(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isAir() ? true : false;
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.TRANSLUCENT;
    }
}