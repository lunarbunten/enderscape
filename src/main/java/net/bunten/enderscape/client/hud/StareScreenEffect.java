package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
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

import java.util.function.Function;

import static net.bunten.enderscape.client.EnderscapeClient.MAX_STARE_STICKS;
import static net.minecraft.client.renderer.RenderStateShard.*;

@Environment(EnvType.CLIENT)
public class StareScreenEffect extends HudElement {

    public static final ResourceLocation STATIC_TEXTURE = Enderscape.id("textures/misc/static.png");

    public static final RenderStateShard.TransparencyStateShard SCREEN_EFFECT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("enderscape_stare_screen_effect_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final Function<ResourceLocation, RenderType> SCREEN_EFFECT = Util.memoize(location -> RenderType.create("enderscape_stare_screen_effect",
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

    public StareScreenEffect() {
        super(RenderPhase.BEFORE_HUD);
    }

    public void render(GuiGraphics graphics, DeltaTracker delta) {
        if (client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator() || EnderscapeClient.stareTicks <= 0 || !EnderscapeConfig.getInstance().endermanStaticOverlay) {
            return;
        }

        graphics.pose().pushPose();

        float alpha = Mth.clamp((float) EnderscapeClient.stareTicks / MAX_STARE_STICKS, 0.0F, 0.25F);
        graphics.blit(
                StareScreenEffect::screenEffect,
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
                white(alpha)
        );

        graphics.pose().popPose();
    }

    public void tick() {
        super.tick();
        if (EnderscapeClient.stareTicks > 0 && !client.isPaused()) EnderscapeClient.stareTicks--;
    }
}