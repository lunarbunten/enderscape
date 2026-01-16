package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import static net.bunten.enderscape.client.EnderscapeClient.MAX_STARE_STICKS;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;

@Environment(EnvType.CLIENT)
public class StareScreenEffect extends HudElement {

    public static final Identifier STATIC_TEXTURE = Enderscape.id("textures/misc/static.png");

    public static final RenderPipeline SCREEN_EFFECT_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/stare_screen_effect"))
                    .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA))
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .build()
    );

    public StareScreenEffect() {
        super(RenderPhase.BEFORE_HUD);
    }

    private float alpha = 0, previousAlpha = 0;

    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator() || EnderscapeClient.stareTicks <= 0 || !EnderscapeConfig.getInstance().endermanStaticOverlay) {
            return;
        }

        graphics.pose().pushMatrix();

        graphics.blit(
                SCREEN_EFFECT_PIPELINE,
                STATIC_TEXTURE,
                0,
                0,
                client.player.getRandom().nextInt(graphics.guiWidth() / 2),
                client.player.getRandom().nextInt(graphics.guiHeight() / 2),
                graphics.guiWidth(),
                graphics.guiHeight(),
                graphics.guiWidth() / 2,
                graphics.guiHeight() / 2,
                128,
                128,
                white(Mth.lerp(tracker.getGameTimeDeltaPartialTick(false), previousAlpha, alpha))
        );

        graphics.pose().popMatrix();
    }

    public void tick() {
        super.tick();

        if (EnderscapeClient.stareTicks > 0 && !client.isPaused()) {
            previousAlpha = alpha;
            alpha = Mth.clamp((float) EnderscapeClient.stareTicks-- / MAX_STARE_STICKS, 0.0F, 0.25F);
        }
    }
}