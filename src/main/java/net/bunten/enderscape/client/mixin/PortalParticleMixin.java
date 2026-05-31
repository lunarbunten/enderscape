package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.registry.EnderscapeParticleProviders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PortalParticle.class)
public abstract class PortalParticleMixin extends SingleQuadParticle {
    protected PortalParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, d, e, f, textureAtlasSprite);
    }

    @Inject(method = "getLightCoords", at = @At("RETURN"), cancellable = true)
    private void setupColor(float f, CallbackInfoReturnable<Integer> cir) {
        if (EnderscapeConfig.getInstance().portalParticleEmissive) cir.setReturnValue(Math.max(EnderscapeParticleProviders.scaledLight(0.6F), cir.getReturnValueI()));
    }
}