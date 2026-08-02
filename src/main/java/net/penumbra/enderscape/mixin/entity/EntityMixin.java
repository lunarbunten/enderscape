package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.DriftJellyBlock;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    public abstract boolean canSimulateMovement();

    @Unique
    private final Entity self = (Entity) (Object) this;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void Enderscape$tick(CallbackInfo info) {
        MagniaAffected.tickCooldown(self);
        VoidManager.tickTail(self);
    }

    @Inject(method = "turn", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelTurning(double d, double e, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(self)) info.cancel();
    }

    @ModifyConstant(method = "checkBelowWorld", constant = @Constant(intValue = 64))
    public int Enderscape$changeBelowWorldPoint(int original) {
        return -EnderscapeConfig.getInstance().outerVoidHeightTreshold;
    }

    @ModifyReturnValue(method = "canSpawnSprintParticle", at = @At(value = "RETURN"))
    public boolean Enderscape$preventSprintParticles(boolean original) {
        return original && !EnderscapeMobEffects.isStunned(self);
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

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;restituteMovementAfterCollisions(Lnet/minecraft/world/level/block/state/BlockState;ZZLnet/minecraft/world/phys/Vec3;)V"))
    private boolean Enderscape$handleLegacyBounce(Entity instance, BlockState effectState, boolean xCollision, boolean zCollision, Vec3 movement) {
        if (effectState.getBlock() instanceof DriftJellyBlock block) {
            block.updateEntityMovementAfterFallOn((Entity) (Object) this);
            return false;
        }
        return true;
    }
}