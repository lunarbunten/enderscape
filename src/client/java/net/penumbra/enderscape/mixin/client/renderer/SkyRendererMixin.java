package net.penumbra.enderscape.mixin.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.renderer.level.EnderscapeSkybox;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @Unique private static final Identifier ENDERSCAPE_END_FLASH = Enderscape.id("end_flash");
    @Unique private EnderscapeSkybox enderscape$skybox;
    @Unique private GpuBuffer enderscape$flashBuffer;

    @Shadow
    private static GpuBuffer buildCelestialQuad(String string, TextureAtlasSprite textureAtlasSprite) {
        return null;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void Enderscape$init(final TextureManager textureManager, final AtlasManager atlasManager, CallbackInfo info) {
        enderscape$flashBuffer = buildCelestialQuad("Enderscape flash quad", atlasManager.getAtlasOrThrow(AtlasIds.CELESTIALS).getSprite(ENDERSCAPE_END_FLASH));
        enderscape$skybox = new EnderscapeSkybox();
    }

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void Enderscape$renderEndSky(CallbackInfo info) {
        Minecraft minecraft = Minecraft.getInstance();

        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled && minecraft.level != null)  {
            enderscape$skybox.render(new PoseStack(), minecraft.level, minecraft.gameRenderer.getMainCamera(), minecraft.getDeltaTracker());
            info.cancel();
        }
    }

    @ModifyArgs(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lorg/joml/Vector4f;<init>(FFFF)V"))
    private void Enderscape$changeColor(Args args) {
        if (EndFlashParameters.improved())  {
            float intensity = args.get(3);
            Vector3f color = EndFlashParameters.getSkyLightColor();

            args.set(0, color.x * intensity);
            args.set(1, color.y * intensity);
            args.set(2, color.z * intensity);
        }
    }

    @ModifyArg(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V"), index = 1)
    private GpuBuffer Enderscape$changeEndFlashBuffer(GpuBuffer original) {
        return EndFlashParameters.improved() ? enderscape$flashBuffer : original;
    }

    @Inject(method = "close", at = @At("TAIL"))
    public void Enderscape$close(CallbackInfo info) {
        enderscape$flashBuffer.close();
        enderscape$skybox.close();
    }
}