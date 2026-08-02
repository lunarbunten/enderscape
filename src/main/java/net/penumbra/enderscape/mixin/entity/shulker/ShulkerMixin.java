package net.penumbra.enderscape.mixin.entity.shulker;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Shulker.class)
public abstract class ShulkerMixin extends AbstractGolem {

    @Shadow protected abstract boolean isClosed();

    protected ShulkerMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private final Shulker shulker = (Shulker) (Object) this;

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void Enderscape$hurtServer(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (Enderscape$isPiercingArrow(source)) info.setReturnValue(SoundEvents.SHULKER_HURT);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void Enderscape$hurtServer(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (Enderscape$isPiercingArrow(source)) info.setReturnValue(super.hurtServer(level, source, amount));
    }

    @Unique
    private boolean Enderscape$isPiercingArrow(DamageSource source) {
        return EnderscapeConfig.getInstance().shulkerHurtByPiercing && isClosed() && source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0;
    }

    @Inject(method = "teleportSomewhere", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelTeleport(CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(shulker)) info.setReturnValue(false);
    }
}