package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @Unique
    private static final Identifier ENDERSCAPE_END_FLASH = Enderscape.id("end_flash");

    @Unique
    private final Minecraft client = Minecraft.getInstance();

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void renderEndSky(CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled && client.level != null)  {
            EnderscapeSkybox.render(new PoseStack(), client.level, client.gameRenderer.mainCamera(), client.getDeltaTracker());
            ci.cancel();
        }
    }

    @ModifyArgs(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lorg/joml/Vector4f;<init>(FFFF)V"))
    private void Enderscape$changeColor(Args args) {
        if (EnderscapeConfig.getInstance().flashUpdatedVisuals)  {
            float intensity = args.get(3);
            Vector3f color = EndFlashParameters.getSkyLightColor(client);
            args.set(0, color.x * intensity);
            args.set(1, color.y * intensity);
            args.set(2, color.z * intensity);
        }
    }

    @Inject(method = "buildEndFlashQuad", at = @At("RETURN"), cancellable = true)
    private static void Enderscape$buildEndFlashQuad(TextureAtlas atlas, CallbackInfoReturnable<GpuBuffer> info) {
        if (EnderscapeConfig.getInstance().flashUpdatedVisuals) info.setReturnValue(buildCelestialQuad("End flash quad", atlas.getSprite(ENDERSCAPE_END_FLASH)));
    }

    @Shadow
    private static GpuBuffer buildCelestialQuad(String string, TextureAtlasSprite textureAtlasSprite) {
        return null;
    }
}