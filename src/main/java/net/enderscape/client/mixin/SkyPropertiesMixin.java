package net.enderscape.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.End.class)
public abstract class SkyPropertiesMixin extends SkyProperties {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public SkyPropertiesMixin(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
        super(cloudsHeight, alternateSkyColor, skyType, brightenLighting, darkened);
    }

    @Inject(method = "adjustFogColor", at = @At("HEAD"), cancellable = true)
    public void ESadjustFogColor(Vec3d color, float sunHeight, CallbackInfoReturnable<Vec3d> info) {
        info.setReturnValue(color);
    }

    @Inject(method = "useThickFog", at = @At("HEAD"), cancellable = true)
    public void useThickFog(int camX, int camY, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(client.inGameHud.getBossBarHud().shouldPlayDragonMusic());
    }
}