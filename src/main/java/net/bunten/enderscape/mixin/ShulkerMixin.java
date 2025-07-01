package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
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

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void Enderscape$hurtServer(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (Enderscape$isPiercingArrow(source)) info.setReturnValue(SoundEvents.SHULKER_HURT);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void Enderscape$hurtServer(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (Enderscape$isPiercingArrow(source)) info.setReturnValue(super.hurt(source, amount));
    }

    @Unique
    private boolean Enderscape$isPiercingArrow(DamageSource source) {
        return EnderscapeConfig.getInstance().shulkerHurtByPiercing && isClosed() && source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0;
    }
}