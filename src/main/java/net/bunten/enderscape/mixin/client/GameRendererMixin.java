package net.bunten.enderscape.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.hud.HudElements;
import net.bunten.enderscape.client.hud.RenderPhase;
import net.bunten.enderscape.entity.AbstractDrifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    abstract void loadEffect(ResourceLocation resourceLocation);

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"
        ),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupFor3DItems()V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderConfusionOverlay(F)V")
        )
    )
    public void render(float f, long l, boolean bl, CallbackInfo info) {
        HudElements.ELEMENTS.stream().filter((element) -> element.phase == RenderPhase.BEFORE_HUD).forEach((element) -> {
            element.render(new PoseStack(), minecraft.getDeltaFrameTime());
        });
    }

    @Inject(method = "checkEntityPostEffect", at = @At("TAIL"), cancellable = true)
    public void checkEntityPostEffect(@Nullable Entity entity, CallbackInfo info) {
        if (entity instanceof AbstractDrifter) {
            loadEffect(Enderscape.id("shaders/post/drifter.json"));
        }
    }
}