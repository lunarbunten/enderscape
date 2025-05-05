package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.bunten.enderscape.EnderscapeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @Unique
    private final Minecraft client = Minecraft.getInstance();

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void renderEndSky(CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled && client.level != null)  {
            EnderscapeSkybox.render(new PoseStack(), client.level, client.gameRenderer.getMainCamera(), client.getDeltaTracker());
            ci.cancel();
        }
    }
}