package net.penumbra.enderscape.manager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.item.component.DashJump;
import net.penumbra.enderscape.item.component.value.GlidingBasedValue;
import net.penumbra.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.entity.EnderscapeStats;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;

import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.DASH_JUMP;
import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.DASH_JUMP_CHARGING_TICKS;
import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.DASH_JUMP_TICKS;

public class DashJumpManager {

    public static int dashJumpChargedDuration(LivingEntity entity) {
        return entity.getAttachedOrElse(DASH_JUMP_CHARGING_TICKS, 0);
    }

    public static boolean dashJumping(LivingEntity entity) {
        return entity.hasAttached(DASH_JUMP_TICKS);
    }

    public static int dashJumpDuration(LivingEntity entity) {
        return entity.getAttachedOrElse(DASH_JUMP_TICKS, 0);
    }

    public static void tryApply(ServerPlayer player) {
        ItemStack item = player.getUseItem();

        if (player.isUsingItem() && item.has(EnderscapeDataComponents.DASH_JUMP)) {
            DashJump.apply(player.level(), player, item);
        }
    }

    public static boolean hasFoodOrIsCreative(Player player) {
        return player.getAbilities().instabuild || player.getFoodData().getFoodLevel() > 6.0F;
    }

    public static void tryAwardStatistics(ServerPlayer player, boolean didNotMove, double x, double z) {
        if (!player.isPassenger() && !didNotMove && dashJumping(player)) {
            int i = Math.round((float) Math.sqrt(x * x + z * z) * 100.0F);
            if (i > 0) player.awardStat(EnderscapeStats.RUBBLE_SHIELD_DASH_ONE_CM, i);
        }
    }

    public static void tickHead(LivingEntity entity) {
        if (dashJumping(entity) && shouldStopDashing(entity)) entity.removeAttached(DASH_JUMP_TICKS);
    }

    public static void tickTail(LivingEntity entity) {
        tickCharging(entity);

        if (dashJumping(entity)) {
            Vec3 movement = entity.position().subtract(entity.oldPosition()).scale(-1.0F);
            int jumpDuration = dashJumpDuration(entity);

            trySlowMovement(entity);
            createDashJumpParticles(entity, jumpDuration, movement);

            entity.modifyAttached(DASH_JUMP_TICKS, value -> value - 1);

            if (jumpDuration <= 0 || (jumpDuration < 50 && movement.lengthSqr() < 0.3)) entity.removeAttached(DASH_JUMP_TICKS);
        }
    }

    public static float getChargeDuration(LivingEntity entity) {
        if (entity.getUseItem() != null && entity.getUseItem().has(DASH_JUMP)) {
            GlidingBasedValue chargeDuration = entity.getUseItem().get(DASH_JUMP).charge().duration();
            return chargeDuration.calculate(entity).asFloat() * 20.0F;
        }

        return 0.0F;
    }

    public static float getChargeProgress(LivingEntity entity) {
        float value = 1.0F;

        float elapsed = dashJumpChargedDuration(entity);
        float duration = getChargeDuration(entity);

        if (duration > 0.0F) {
            if (elapsed > duration) {
                float excess = Math.min((elapsed - duration) / duration, 1.0F);
                value = 1.0F - Ease.inOutQuart(excess) * 0.2F;
            } else {
                value = Ease.outQuart(elapsed / duration);
            }
        }

        return Mth.clamp(value, 0.0F, 1.0F);
    }

    private static boolean shouldStopDashing(LivingEntity entity) {
        return entity.onGround() || entity.getVehicle() != null || entity.isInLiquid() || entity.isSpectator();
    }

    private static void tickCharging(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        if (entity.getUseItem().has(EnderscapeDataComponents.DASH_JUMP)) {
            entity.setAttached(DASH_JUMP_CHARGING_TICKS, dashJumpChargedDuration(entity) + 1);
        } else {
            entity.removeAttached(DASH_JUMP_CHARGING_TICKS);
        }
    }

    private static void trySlowMovement(LivingEntity entity) {
        if (entity.isShiftKeyDown()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.92, 1, 0.92));
        }
    }

    private static void createDashJumpParticles(LivingEntity entity, int jumpDuration, Vec3 movement) {
        RandomSource random = entity.getRandom();

        double x = random.nextGaussian() * 0.02 + movement.x;
        double y = random.nextGaussian() * 0.02 + movement.y;
        double z = random.nextGaussian() * 0.02 + movement.z;

        if (jumpDuration > 40 && jumpDuration % 5 == 0 && jumpDuration != 60) {
            entity.level().addParticle(new DashJumpShockwaveParticleOptions(
                    movement.toVector3f(),
                    ((float) jumpDuration / 60)),
                    entity.getX(),
                    entity.getY() + (entity.getBbHeight() / 2),
                    entity.getZ(),
                    movement.x,
                    movement.y,
                    movement.z
            );
        }

        entity.level().addParticle(
                EnderscapeParticles.DASH_JUMP_SPARKS,
                entity.getRandomX(1.0) - (movement.x / 2),
                entity.getRandomY() - (movement.y / 2),
                entity.getRandomZ(1.0) - (movement.z / 2),
                x,
                y,
                z
        );
    }
}
