package net.penumbra.enderscape.gui.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.penumbra.enderscape.Enderscape;

@Environment(EnvType.CLIENT)
public class OuterVoidWarningScreenEffect extends EnderscapeHudElement {

    public static final Identifier OUTER_VOID_WARNING_VIGNETTE = Enderscape.id("textures/misc/outer_void_warning_vignette.png");

    private float alpha = 0, previousAlpha = 0;

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker tracker) {
        if (minecraft.player == null || minecraft.options.hideGui || minecraft.player.isSpectator() || alpha <= 0.0F) {
            return;
        }
        
        graphics.pose().pushMatrix();

        float partialTicks = tracker.getGameTimeDeltaPartialTick(false);
        float lerpedAlpha = Mth.lerp(partialTicks, previousAlpha, alpha);

        /*
            Vignette overlay
         */

        int color = ARGB.color(255, ARGB.linearLerp(lerpedAlpha, 0x000000, 0x291035));

        graphics.blit(
                RenderPipelines.GUI_NAUSEA_OVERLAY,
                OUTER_VOID_WARNING_VIGNETTE,
                0,
                0,
                0,
                0,
                graphics.guiWidth(),
                graphics.guiHeight(),
                64,
                64,
                64,
                64,
                color
        );

        graphics.pose().popMatrix();
    }

    @Override
    public void tick() {
        super.tick();

        previousAlpha = alpha;
        variables.outerVoidIntensity = Mth.lerp(0.1F, variables.outerVoidIntensity, 0.0F);
        alpha = Mth.clamp(variables.outerVoidIntensity, 0.0F, 1.0F);
    }

    @Override
    public boolean shouldTick() {
        return super.shouldTick() && variables.outerVoidIntensity > 0.0F;
    }

    @Override
    public void reset() {
        alpha = 0.0F;
    }
}