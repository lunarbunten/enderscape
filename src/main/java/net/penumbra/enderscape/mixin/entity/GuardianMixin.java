package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.monster.Guardian;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Guardian.class)
public abstract class GuardianMixin {

    @Unique
    private final Guardian guardian = (Guardian) (Object) this;

    @ModifyReturnValue(method = "hasActiveAttackTarget", at = @At("RETURN"))
    public boolean Enderscape$cancelAttackTarget(boolean original) {
        return !EnderscapeMobEffects.isStunned(guardian) && original;
    }
}