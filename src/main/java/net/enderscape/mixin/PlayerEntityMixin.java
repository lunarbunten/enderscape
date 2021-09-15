package net.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.enderscape.items.DriftBootsItem;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    PlayerEntity mob = (PlayerEntity) (Object) this;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "startFallFlying")
    public void ESstartFallFlying(CallbackInfo info) {
        playSound(EndSounds.ITEM_ELYTRA_START, 1, EndMath.nextFloat(mob.getRandom(), 0.8F, 1.2F));
    }

    @Unique
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        int amount = super.computeFallDamage(fallDistance, damageMultiplier);
        boolean bl = mob.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof DriftBootsItem;
        return bl ? amount / 2 : amount;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void EStick(CallbackInfo info) {
        if (!mob.isSpectator()) {
            boolean bl = !mob.isSneaking() && !mob.isOnGround() && !mob.isFallFlying() && !mob.hasVehicle() && !mob.getAbilities().flying && !mob.isInsideWaterOrBubbleColumn() && !mob.hasStatusEffect(StatusEffects.LEVITATION);
            if (mob.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof DriftBootsItem && bl) {
                Vec3d vel = mob.getVelocity();

                double x = vel.x;
                double y = vel.y;
                double z = vel.z;

                x /= 0.91F + 0.05F;
                y += 0.03F;
                z /= 0.91F + 0.05F;

                mob.setVelocity(x, y, z);
            }
        }
    }
}