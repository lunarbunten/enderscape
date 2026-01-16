package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AtmosphericFogEnvironment.class)
public abstract class AtmosphericFogEnvironmentMixin {

    @Inject(method = "setupFog", at = @At("TAIL"))
    public void Enderscape$getBrightnessDependentFogColor(FogData data, Camera camera, ClientLevel level, float f, DeltaTracker tracker, CallbackInfo info) {
        if (level.dimension() == Level.END && !Minecraft.getInstance().gui.getBossOverlay().shouldCreateWorldFog()) {
            data.environmentalEnd = Math.max(96.0F, Math.min(data.environmentalEnd, f * camera.attributeProbe().getValue(EnderscapeEnvironmentAttributes.FOG_END_DENSITY, tracker.getGameTimeDeltaPartialTick(false))));
        }
    }
}