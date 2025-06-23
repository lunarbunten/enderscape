package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AtmosphericFogEnvironment.class)
public abstract class AtmosphericFogEnvironmentMixin {

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    public void Enderscape$getBrightnessDependentFogColor(FogData data, Entity entity, BlockPos pos, ClientLevel level, float f, DeltaTracker tracker, CallbackInfo info) {
        if (level.dimension() == Level.END && EnderscapeConfig.getInstance().skyboxAddDynamicFogDensity) {
            data.environmentalStart = (f * 0.05F) / EnderscapeSkybox.fogStartDensity;
            data.environmentalEnd = f / EnderscapeSkybox.fogEndDensity;
            data.skyEnd = f;
            data.cloudEnd = Minecraft.getInstance().options.cloudRange().get() * 16;
            info.cancel();
        }
    }
}