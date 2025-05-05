package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LightLayer;

import java.util.function.Function;

import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;
import static net.minecraft.client.renderer.RenderStateShard.TextureStateShard;

@Environment(EnvType.CLIENT)
public class MirrorScreenEffect extends HudElement {

    public static final ResourceLocation OVERLAY_TEXTURE = Enderscape.id("textures/misc/overlay.png");
    public static final ResourceLocation VIGNETTE_TEXTURE = Enderscape.id("textures/misc/vignette.png");

    public static final RenderPipeline SCREEN_EFFECT_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/mirror_screen_effect"))
                    .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA))
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .build()
    );

    private static final Function<ResourceLocation, RenderType> SCREEN_EFFECT = Util.memoize(
            resourceLocation -> RenderType.create(
                    "enderscape_mirror_screen_effect",
                    786432,
                    SCREEN_EFFECT_PIPELINE,
                    RenderType.CompositeState.builder().setTextureState(new TextureStateShard(resourceLocation, TriState.DEFAULT, false)).createCompositeState(false)
            )
    );

    public static RenderType screenEffect(ResourceLocation resourceLocation) {
        return SCREEN_EFFECT.apply(resourceLocation);
    }

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

        graphics.blit(MirrorScreenEffect::screenEffect, OVERLAY_TEXTURE, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 64, 64, 64, 64, white(overlayAlpha));
        graphics.blit(MirrorScreenEffect::screenEffect, VIGNETTE_TEXTURE, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(),256, 256, 256, 256, white(vignetteAlpha));

        graphics.pose().popPose();
    }

    public void tick() {
        super.tick();
        if (EnderscapeClient.postMirrorUseTicks > 0 && !client.isPaused()) EnderscapeClient.postMirrorUseTicks--;
    }
}