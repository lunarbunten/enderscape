package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

@Environment(EnvType.CLIENT)
public class MirrorScreenEffect extends HudElement {

    public static final ResourceLocation OVERLAY_TEXTURE = Enderscape.id("textures/misc/overlay.png");
    public static final ResourceLocation VIGNETTE_TEXTURE = Enderscape.id("textures/misc/vignette.png");

    public MirrorScreenEffect() {
        super(RenderPhase.BEFORE_HUD);
    }

    public void render(GuiGraphics graphics, DeltaTracker delta) {
        if (client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator() || EnderscapeClient.postMirrorUseTicks <= 0 || !config.mirrorScreenEffectEnabled) {
            return;
        }
        
        graphics.pose().pushPose();

        float light = Math.max(0.3F, client.level.getBrightness(LightLayer.SKY, client.player.blockPosition()) / 15.0F);
        float overlayAlpha = Mth.clamp((EnderscapeClient.postMirrorUseTicks / 40.0F) * (config.mirrorScreenEffectOverlayIntensity / 100.0F) * light, 0.0F, 1.0F);
        float vignetteAlpha = Mth.clamp((EnderscapeClient.postMirrorUseTicks / 60.0F) * (config.mirrorScreenEffectVignetteIntensity / 100.0F) * light, 0.0F, 1.0F);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);

        graphics.setColor(1, 1, 1, overlayAlpha);
        graphics.blit(OVERLAY_TEXTURE, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());

        graphics.setColor(1, 1, 1, vignetteAlpha);
        graphics.blit(VIGNETTE_TEXTURE, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        graphics.pose().popPose();
    }

    public void tick() {
        super.tick();
        if (EnderscapeClient.postMirrorUseTicks > 0 && !client.isPaused()) EnderscapeClient.postMirrorUseTicks--;
    }
}