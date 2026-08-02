package net.penumbra.enderscape.gui.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.penumbra.enderscape.registry.renderer.EnderscapeRenderPipelines;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class LodestoneTeleportationScreenEffect extends EnderscapeHudElement {

    private float alpha = 0, previousAlpha = 0;

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker tracker) {
        if (minecraft.player == null || minecraft.options.hideGui || minecraft.player.isSpectator() || variables.lodestoneTeleportationTicks <= 0 || config.mirrorScreenEffectIntensity <= 0) {
            return;
        }
        
        graphics.pose().pushMatrix();

        float partialTicks = tracker.getGameTimeDeltaPartialTick(false);
        float lerpedAlpha = Mth.lerp(partialTicks, previousAlpha, alpha);

        variables.lodestoneTeleportationOverlayTexture.ifPresent((texture) -> graphics.blit(EnderscapeRenderPipelines.ALPHA_BASED_OVERLAY, texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 64, 64, 64, 64, ARGB.white(lerpedAlpha)));
        variables.lodestoneTeleportationVignetteTexture.ifPresent((texture) -> graphics.blit(EnderscapeRenderPipelines.ALPHA_BASED_OVERLAY, texture, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 64, 64, 64, 64, ARGB.white(lerpedAlpha)));

        graphics.pose().popMatrix();
    }

    @Override
    public void tick() {
        super.tick();

        previousAlpha = alpha;

        int ticks = variables.lodestoneTeleportationTicks--;

        ClientLevel level = minecraft.level;
        Entity entity = minecraft.getCameraEntity();

        float lightIntensity = 0.3F;

        if (level != null && entity != null) {
            lightIntensity = Math.max(0.3F, ClientsideLightUtil.lightBrightnessAt(level, entity.blockPosition()));
        }

        alpha = Mth.clamp((ticks / 40.0F) * (config.mirrorScreenEffectIntensity / 100.0F) * lightIntensity, 0.0F, 1.0F);
    }

    @Override
    public boolean shouldTick() {
        return super.shouldTick() && variables.lodestoneTeleportationTicks > 0;
    }

    @Override
    public void reset() {
        alpha = 0.0F;
    }
}