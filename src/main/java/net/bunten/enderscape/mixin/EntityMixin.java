package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private final Entity self = (Entity) (Object) this;

    @Inject(method = "turn", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelTurning(double d, double e, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(self)) info.cancel();
    }

    @Inject(method = "canSpawnSprintParticle", at = @At(value = "RETURN"), cancellable = true)
    public void Enderscape$cancelSprintParticles(CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(self)) info.setReturnValue(false);
    }

    @ModifyReturnValue(method = "getPickRadius", at = @At(value = "RETURN"))
    public float Enderscape$expandHitRange(float original) {
        float expanded = 0.35F;

        if (self instanceof Silverfish && EnderscapeConfig.getInstance().silverfishExpandHitRange) {
            return expanded;
        }
        if (self instanceof Endermite && EnderscapeConfig.getInstance().endermiteExpandHitRange) {
            return expanded;
        }
        if (self instanceof Rubblemite && EnderscapeConfig.getInstance().rubblemiteExpandHitRange) {
            return expanded;
        }

        return original;
    }

    @ModifyArg(method = "playCombinationStepSounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), index = 1)
    public float Enderscape$adjustCombinationStepSoundVolume(float original) {
        return Math.max(0, original * (float) EnderscapeAttributes.getStealthMultiplier(self));
    }

    @ModifyArg(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), index = 1)
    public float Enderscape$adjustStepSoundVolume(float original) {
        return Math.max(0, original * (float) EnderscapeAttributes.getStealthMultiplier(self));
    }

    @ModifyArg(method = "playMuffledStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), index = 1)
    public float Enderscape$adjustMuffledStepSoundVolume(float original) {
        return Math.max(0, original * (float) EnderscapeAttributes.getStealthMultiplier(self));
    }
}