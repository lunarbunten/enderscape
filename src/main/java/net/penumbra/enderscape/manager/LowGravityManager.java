package net.penumbra.enderscape.manager;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.item.EnderscapeItems;

public class LowGravityManager {

    public static void tickTail(LivingEntity entity) {
        if (allowPhysics(entity)) {
            Vec3 movement = entity.getDeltaMovement();
            double maxSpeed = 2, frictionMod = Math.min(1, 0.96 + Math.max(0, (Math.hypot(movement.x, movement.z) - maxSpeed) / maxSpeed) * 0.5);
            entity.setDeltaMovement(movement.x / frictionMod, movement.y, movement.z / frictionMod);
        }
    }

    private static boolean allowPhysics(LivingEntity entity) {
        return !disallowPhysics(entity) && (entity.getItemBySlot(EquipmentSlot.LEGS).is(EnderscapeItems.DRIFT_LEGGINGS) || entity.hasEffect(EnderscapeMobEffects.LOW_GRAVITY));
    }

    private static boolean disallowPhysics(LivingEntity entity) {
        if (entity.isSpectator()) return true;
        if (entity.isShiftKeyDown()) return true;
        if (entity.onGround()) return true;
        if (entity.isFallFlying()) return true;
        if (entity.isPassenger()) return true;
        if (entity.isInLiquid()) return true;
        if (entity.hasEffect(MobEffects.LEVITATION)) return true;
        if (entity instanceof Player player && player.getAbilities().flying) return true;

        return false;
    }
}