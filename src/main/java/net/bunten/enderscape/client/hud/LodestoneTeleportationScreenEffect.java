package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

import static net.bunten.enderscape.client.EnderscapeClient.*;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;

@Environment(EnvType.CLIENT)
public class LodestoneTeleportationScreenEffect extends EnderscapeHudElement {

    public static final RenderPipeline SCREEN_EFFECT_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/lodestone_teleportation_screen_effect"))
                    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
                    .withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA)))
                    .build()
    );

    public LodestoneTeleportationScreenEffect() {
        super(RenderPhase.BEFORE_HUD, Enderscape.id("lodestone_teleportation_effect"));
    }

    private float overlayAlpha = 0, previousOverlayAlpha = 0;
    private float vignetteAlpha = 0, previousVignetteAlpha = 0;

    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker tracker) {
        if (client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator() || lodestoneTeleportationTicks <= 0 || !config.mirrorScreenEffectEnabled) {
            return;
        }
        
        graphics.pose().pushMatrix();

        float delta = tracker.getGameTimeDeltaPartialTick(false);
        float overlay = Mth.lerp(delta, previousOverlayAlpha, overlayAlpha);
        float vignette = Mth.lerp(delta, previousVignetteAlpha, vignetteAlpha);

        lodestoneTeleportationOverlayTexture.ifPresent((texture) -> graphics.blit(SCREEN_EFFECT_PIPELINE, texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 64, 64, 64, 64, white(overlay)));
        lodestoneTeleportationVignetteTexture.ifPresent((texture) -> graphics.blit(SCREEN_EFFECT_PIPELINE, texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 64, 64, 64, 64, white(vignette)));

        graphics.pose().popMatrix();
    }

    public void tick() {
        super.tick();

        if (!client.isPaused() && client.level != null && client.player != null && lodestoneTeleportationTicks > 0) {
            previousOverlayAlpha = overlayAlpha;
            previousVignetteAlpha = vignetteAlpha;

            int ticks = lodestoneTeleportationTicks--;

            float lightIntensity = Math.max(0.3F, client.level.getBrightness(LightLayer.SKY, client.player.blockPosition()) / 15.0F);
            float overlayIntensity = config.mirrorScreenEffectOverlayIntensity / 100.0F;
            float vignetteIntensity = config.mirrorScreenEffectVignetteIntensity / 100.0F;

            overlayAlpha = Mth.clamp((ticks / 40.0F) * overlayIntensity * lightIntensity, 0.0F, 1.0F);
            vignetteAlpha = Mth.clamp((ticks / 60.0F) * vignetteIntensity * lightIntensity, 0.0F, 1.0F);
        }
    }
}