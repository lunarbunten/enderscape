package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin {

    @Unique
    private final Creeper creeper = (Creeper) (Object) this;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void Enderscape$tick(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(creeper)) creeper.setSwellDir(-1);
    }
}