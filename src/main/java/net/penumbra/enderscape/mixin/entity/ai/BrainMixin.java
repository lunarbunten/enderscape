package net.penumbra.enderscape.mixin.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public abstract class BrainMixin<E extends LivingEntity> {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$tick(ServerLevel level, E mob, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(mob)) info.cancel();
    }
}