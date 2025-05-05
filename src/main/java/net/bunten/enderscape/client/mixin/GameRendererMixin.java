package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.client.hud.HudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Final
    @Shadow
    private RenderBuffers renderBuffers;

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"
        ),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupFor3DItems()V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemActivationAnimation(Lnet/minecraft/client/gui/GuiGraphics;F)V")
        )
    )
    public void render(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        EnderscapeClient.HUD_ELEMENTS.stream().filter((element) -> element.phase == HudElement.RenderPhase.BEFORE_HUD).forEach((element) -> {
            element.render(new GuiGraphics(minecraft, renderBuffers.bufferSource()), deltaTracker);
        });
    }
}