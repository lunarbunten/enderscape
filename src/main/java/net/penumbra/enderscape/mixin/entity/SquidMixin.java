package net.penumbra.enderscape.mixin.entity;

import net.minecraft.world.entity.animal.squid.Squid;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Squid.class)
public abstract class SquidMixin {

    @Unique
    private final Squid squid = (Squid) (Object) this;

    @Inject(method = "spawnInk", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelSpawnInk(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(squid)) info.cancel();
    }
}