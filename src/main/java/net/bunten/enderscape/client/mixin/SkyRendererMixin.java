package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
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

    @Shadow protected abstract AbstractTexture getTexture(ResourceLocation resourceLocation);

    @Unique
    private final Minecraft client = Minecraft.getInstance();

    @Unique
    @Nullable
    private AbstractTexture Enderscape$flashTexture;

    @Inject(method = "initTextures", at = @At("TAIL"))
    public void Enderscape$initTextures(CallbackInfo info) {
        Enderscape$flashTexture = getTexture(Enderscape.id("textures/environment/flash.png"));
    }

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void renderEndSky(CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled && client.level != null)  {
            EnderscapeSkybox.render(new PoseStack(), client.level, client.gameRenderer.getMainCamera(), client.getDeltaTracker());
            ci.cancel();
        }
    }

    @ModifyArgs(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lorg/joml/Vector4f;<init>(FFFF)V"))
    private void Enderscape$changeColor(Args args) {
        if (EnderscapeConfig.getInstance().flashUpdatedVisuals)  {
            float intensity = args.get(3);
            Vector4f color = EndFlashParameters.color;
            args.set(0, color.x * intensity);
            args.set(1, color.y * intensity);
            args.set(2, color.z * intensity);
        }
    }

    @ModifyArg(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;bindSampler(Ljava/lang/String;Lcom/mojang/blaze3d/textures/GpuTextureView;)V"))
    private GpuTextureView Enderscape$changeColor(GpuTextureView original) {
        if (EnderscapeConfig.getInstance().flashUpdatedVisuals && Enderscape$flashTexture != null) {
            return Enderscape$flashTexture.getTextureView();
        }
        return original;
    }
}