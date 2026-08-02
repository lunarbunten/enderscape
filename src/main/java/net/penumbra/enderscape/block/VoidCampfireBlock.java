package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;

public class VoidCampfireBlock extends CampfireBlock {
    public VoidCampfireBlock(Properties properties) {
        super(false, 0, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean bl) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().source(EnderscapeDamageTypes.VOID_CAMPFIRE), 1.0F);
        }
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        if (state.getValue(LIT)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,
                        0.5F + random.nextFloat(),
                        random.nextFloat() * 0.7F + 0.6F,
                        false
                );
            }

            for (int i = 0; i < 2; i++) {
                Vec3 center = Vec3.atCenterOf(pos);

                double xx = center.x() + Mth.nextDouble(random, -0.3, 0.3);
                double yy = center.y() + Mth.nextDouble(random, -0.15, 0.15);
                double zz = center.z() + Mth.nextDouble(random, -0.3, 0.3);

                level.addParticle(EnderscapeParticles.VOID_STARS, xx, yy, zz, 0.0, Mth.nextDouble(random, 0.04, 0.06), 0.0);
            }
        }
    }

    @Override
    protected void onProjectileHit(final Level level, final BlockState state, final BlockHitResult blockHit, final Projectile projectile) {
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}