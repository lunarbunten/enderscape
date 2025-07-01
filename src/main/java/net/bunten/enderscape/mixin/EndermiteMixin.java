package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Endermite.class)
public abstract class EndermiteMixin extends Monster {
    protected EndermiteMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getAmbientSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getAmbientSound(CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_AMBIENT.get());
    }

    @Inject(method = "getHurtSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_HURT.get());
    }

    @Inject(method = "getDeathSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getDeathSound(CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_DEATH.get());
    }
}