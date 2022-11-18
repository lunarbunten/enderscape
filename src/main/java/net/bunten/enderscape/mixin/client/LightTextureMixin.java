package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.bunten.enderscape.client.SkyInfo;
import net.bunten.enderscape.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
@Mixin(LightTexture.class)
public abstract class LightTextureMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private boolean isEnd() {
        return minecraft.level != null && minecraft.level.dimension() == Level.END;
    }

    @ModifyArgs(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Vector3f;lerp(Lcom/mojang/math/Vector3f;F)V", ordinal = 1))
    private void changeEndLighting(Args args) {
        if (Config.CLIENT.modifyLightmap() && isEnd()) {
            args.set(0, SkyInfo.lightColor);
            args.set(1, SkyInfo.lightIntensity);
        }
    }

    @ModifyArgs(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Vector3f;lerp(Lcom/mojang/math/Vector3f;F)V", ordinal = 5))
    private void fixGamma(Args args) {
        if (Config.CLIENT.modifyLightmap() && isEnd()) {
            Float value = args.get(1);
            args.set(1, value * 0.5F);
        }
    }
}