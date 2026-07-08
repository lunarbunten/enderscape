package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.enderscape.Enderscape;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static net.bunten.enderscape.client.EnderscapeClient.*;

@OnlyIn(Dist.CLIENT)
public class LodestoneTeleportationScreenEffect extends HudElement {

    public LodestoneTeleportationScreenEffect() {
        super(Enderscape.id("lodestone_teleport_screen"), RenderPhase.BEFORE_HUD);
    }

    private float overlayAlpha = 0, previousOverlayAlpha = 0;
    private float vignetteAlpha = 0, previousVignetteAlpha = 0;

    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().options.hideGui || !Minecraft.getInstance().options.getCameraType().isFirstPerson() || Minecraft.getInstance().player.isSpectator() || lodestoneTeleportationTicks <= 0 || !config.mirrorScreenEffectEnabled) {
            return;
        }
        
        graphics.pose().pushPose();

        float delta = tracker.getGameTimeDeltaPartialTick(false);
        float overlay = Mth.lerp(delta, previousOverlayAlpha, overlayAlpha);
        float vignette = Mth.lerp(delta, previousVignetteAlpha, vignetteAlpha);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);

        lodestoneTeleportationOverlayTexture.ifPresent((texture) -> {
            graphics.setColor(1, 1, 1, overlay);
            graphics.blit(texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());
        });

        lodestoneTeleportationVignetteTexture.ifPresent((texture) -> {
            graphics.setColor(1, 1, 1, vignette);
            graphics.blit(texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());
        });

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        graphics.pose().popPose();
    }

    public void tick() {
        super.tick();

        if (!Minecraft.getInstance().isPaused() && Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && lodestoneTeleportationTicks > 0) {
            previousOverlayAlpha = overlayAlpha;
            previousVignetteAlpha = vignetteAlpha;

            int ticks = lodestoneTeleportationTicks--;

            float lightIntensity = Math.max(0.3F, Minecraft.getInstance().level.getBrightness(LightLayer.SKY, Minecraft.getInstance().player.blockPosition()) / 15.0F);
            float overlayIntensity = config.mirrorScreenEffectOverlayIntensity / 100.0F;
            float vignetteIntensity = config.mirrorScreenEffectVignetteIntensity / 100.0F;

            overlayAlpha = Mth.clamp((ticks / 40.0F) * overlayIntensity * lightIntensity, 0.0F, 1.0F);
            vignetteAlpha = Mth.clamp((ticks / 60.0F) * vignetteIntensity * lightIntensity, 0.0F, 1.0F);
        }
    }
}