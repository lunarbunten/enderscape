package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PortalParticle.class)
public abstract class PortalParticleMixin extends TextureSheetParticle {

    protected PortalParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    @Inject(method = "getLightColor", at = @At("RETURN"), cancellable = true)
    private void setupColor(float f, CallbackInfoReturnable<Integer> cir) {
        if (EnderscapeConfig.getInstance().portalParticleEmissive) cir.setReturnValue(Math.max(160, cir.getReturnValueI()));
    }
}