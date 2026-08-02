package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Pufferfish.class)
public abstract class PufferfishMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fish/Pufferfish;isEffectiveAi()Z"))
    public boolean Enderscape$cancelPuffing(Pufferfish instance, Operation<Boolean> original) {
        return !EnderscapeMobEffects.isStunned(instance) && original.call(instance);
    }
}