package net.penumbra.enderscape.mixin.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.util.ClientsideLightUtil;
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
    private void setupColor(float f, CallbackInfoReturnable<Integer> info) {
        if (EnderscapeConfig.getInstance().portalParticleEmissive) info.setReturnValue(Math.max(ClientsideLightUtil.scaledLight(0.6F), info.getReturnValueI()));
    }
}