package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LightLayer;

import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

@Environment(EnvType.CLIENT)
public class MirrorScreenEffect extends HudElement {

    public static final ResourceLocation OVERLAY_TEXTURE = Enderscape.id("textures/misc/overlay.png");
    public static final ResourceLocation VIGNETTE_TEXTURE = Enderscape.id("textures/misc/vignette.png");

    public static final RenderStateShard.TransparencyStateShard SCREEN_EFFECT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("enderscape_mirror_screen_effect_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final Function<ResourceLocation, RenderType> SCREEN_EFFECT = Util.memoize(location -> RenderType.create("enderscape_mirror_screen_effect",
                    DefaultVertexFormat.POSITION_TEX_COLOR,
                    VertexFormat.Mode.QUADS,
                    786432,
                    RenderType.CompositeState.builder()
                            .setTextureState(new TextureStateShard(location, TriState.DEFAULT, false))
                            .setShaderState(POSITION_TEXTURE_COLOR_SHADER)
                            .setTransparencyState(SCREEN_EFFECT_TRANSPARENCY)
                            .setDepthTestState(NO_DEPTH_TEST)
                            .setWriteMaskState(COLOR_WRITE)
                            .createCompositeState(false)
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