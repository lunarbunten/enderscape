package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private final LivingEntity mob = (LivingEntity) (Object) this;

    @Inject(at = @At("TAIL"), method = "tick")
    private void EStick(CallbackInfo info) {
        if (hasDriftLeggings() && !shouldCancelPhysics()) {
            Vec3d vel = mob.getVelocity();

            double x = vel.x;
            double y = vel.y;
            double z = vel.z;

            x /= 0.91F + 0.05F;
            z /= 0.91F + 0.05F;
            y += mob.hasStatusEffect(StatusEffects.SLOW_FALLING) ? (y > 0 ? 0.03F : 0) : 0.03F;

            mob.setVelocity(x, y, z);
        }
    }

    @Inject(at = @At("RETURN"), method = "computeFallDamage", cancellable = true)
    private void computeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        if (hasDriftLeggings()) {
            int value = info.getReturnValue();
            info.setReturnValue(value - 2);
        }
    }

    protected boolean hasDriftLeggings() {
        return mob.getEquippedStack(EquipmentSlot.LEGS).isOf(EnderscapeItems.DRIFT_LEGGINGS);
    }

    protected boolean shouldCancelPhysics() {
        if (mob.isSpectator()) {
            return true;
        }
        if (mob.isSneaking()) {
            return true;
        }
        if (mob.isOnGround()) {
            return true;
        }
        if (mob.isFallFlying()) {
            return true;
        }
        if (mob.hasVehicle()) {
            return true;
        }
        if (mob.isInsideWaterOrBubbleColumn()) {
            return true;
        }
        if (mob.hasStatusEffect(StatusEffects.LEVITATION)) {
            return true;
        }
        if (mob instanceof PlayerEntity player && player.getAbilities().flying) {
            return true;
        }

        return false;
    }
}