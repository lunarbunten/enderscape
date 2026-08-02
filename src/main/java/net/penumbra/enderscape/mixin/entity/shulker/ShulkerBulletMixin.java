package net.penumbra.enderscape.mixin.entity.shulker;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Projectile {

    @Shadow protected abstract void destroy();
    @Shadow private @Nullable EntityReference<Entity> finalTarget;

    public ShulkerBulletMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void Enderscape$destroyInvalidBullet(CallbackInfo info) {
        if (level().isClientSide()) return;

        EnderscapeConfig config = EnderscapeConfig.getInstance();
        Entity target = EntityReference.get(finalTarget, level(), Entity.class);
        Entity owner = getOwner();

        boolean pastTimeLimit = config.shulkerBulletEnforceTimeLimit > 0 && tickCount > config.shulkerBulletEnforceTimeLimit * 20;
        boolean pastDistance = config.shulkerBulletEnforceDistanceLimit > 0 && target != null && distanceTo(target) >= config.shulkerBulletEnforceDistanceLimit;
        boolean ownerInvalid = config.shulkerBulletEnforceOwnerLimit && (owner == null || !owner.isAlive());

        if (pastTimeLimit || pastDistance || ownerInvalid) {
            destroy();
            info.cancel();
        }
    }

    @ModifyArg(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
    protected MobEffectInstance Enderscape$rebalanceLevitation(MobEffectInstance original) {
        return EnderscapeConfig.getInstance().shulkerBulletRebalanceLevitation ? new MobEffectInstance(MobEffects.LEVITATION, 20 * 4, 2) : original;
    }
}