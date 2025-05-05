package net.bunten.enderscape.mixin;

import com.google.common.base.MoreObjects;
import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Projectile {

    private final ShulkerBullet bullet = (ShulkerBullet) (Object) this;

    @Shadow protected abstract void destroy();

    @Shadow private @Nullable Entity finalTarget;

    public ShulkerBulletMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void isValid(CallbackInfo info) {
        boolean pastTimeLimit = EnderscapeConfig.getInstance().shulkerBulletEnforceTimeLimit > 0 && tickCount > EnderscapeConfig.getInstance().shulkerBulletEnforceTimeLimit * 20;
        boolean pastDistance = EnderscapeConfig.getInstance().shulkerBulletEnforceDistanceLimit > 0 && finalTarget != null && distanceTo(finalTarget) >= EnderscapeConfig.getInstance().shulkerBulletEnforceDistanceLimit;
        boolean ownerInvalid = EnderscapeConfig.getInstance().shulkerBulletEnforceOwnerLimit && (getOwner() == null || !getOwner().isAlive());

        if (pastTimeLimit || pastDistance || ownerInvalid) {
            destroy();
            info.cancel();
        }
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
    protected boolean onHitEntity(LivingEntity instance, MobEffectInstance mobEffectInstance, Entity entity) {
        if (EnderscapeConfig.getInstance().shulkerBulletRebalanceLevitation) return instance.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20 * 4, 2), MoreObjects.firstNonNull(entity, bullet));
        return instance.addEffect(mobEffectInstance, MoreObjects.firstNonNull(entity, bullet));
    }
}