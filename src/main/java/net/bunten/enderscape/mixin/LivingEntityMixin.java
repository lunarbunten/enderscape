package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/*
 *  Drift Leggings functionality
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    private final LivingEntity mob = (LivingEntity) (Object) this;

    @Unique
    protected boolean hasDriftLeggings() {
        return mob.getItemBySlot(EquipmentSlot.LEGS).is(EnderscapeItems.DRIFT_LEGGINGS);
    }

    @Unique
    protected boolean shouldCancelPhysics() {
        if (mob.isSpectator()) return true;
        if (mob.isShiftKeyDown()) return true;
        if (mob.isOnGround()) return true;
        if (mob.isFallFlying()) return true;
        if (mob.isPassenger()) return true;
        if (mob.isInWaterOrBubble()) return true;
        if (mob.hasEffect(MobEffects.LEVITATION)) return true;
        if (mob instanceof Player player && player.getAbilities().flying) return true;
        return false;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void EStick(CallbackInfo info) {
        if (hasDriftLeggings() && !shouldCancelPhysics()) {
            Vec3 vel = mob.getDeltaMovement();

            double x = vel.x;
            double y = vel.y;
            double z = vel.z;

            x /= 0.91F + 0.05F;
            z /= 0.91F + 0.05F;
            y += mob.hasEffect(MobEffects.SLOW_FALLING) ? (y > 0 ? 0.03F : 0) : 0.03F;

            mob.setDeltaMovement(x, y, z);
        }
    }

    @Inject(at = @At("RETURN"), method = "calculateFallDamage", cancellable = true)
    private void calculateFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        if (hasDriftLeggings()) {
            int value = info.getReturnValue();
            info.setReturnValue(value - 2);
        }
    }
}