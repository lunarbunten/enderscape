package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.LightLayer;

@Environment(EnvType.CLIENT)
public class MirrorScreenEffect extends HudElement {
        
    public MirrorScreenEffect() {
        super(RenderPhase.BEFORE_HUD);
    }

    public void render(PoseStack matrix, float delta) {
        if (client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator() || EnderscapeClient.postMirrorUseTicks <= 0 || !Config.CLIENT.displayMirrorOverlay()) {
            return;
        }
        
        matrix.pushPose();

        float light = ((float) (client.level.getBrightness(LightLayer.SKY, client.player.blockPosition()))) / 15;
        light = Math.max(0.3F, light);

        float alpha = ((float) EnderscapeClient.postMirrorUseTicks) / 40;
        float alpha2 = ((float) EnderscapeClient.postMirrorUseTicks) / 60;

        float opacity = ((float) (Config.CLIENT.mirrorOverlayIntensity())) / 100;
        float opacity2 = ((float) (Config.CLIENT.mirrorVignetteIntensity())) / 100;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, alpha * opacity * light);
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/gui/mirror/overlay.png"));
        blit(matrix, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, alpha2 * opacity2 * light);
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/gui/mirror/vignette.png"));
        blit(matrix, 0, 0, 0, 0, width, height, width, height);

        matrix.popPose();
    }

    public void tick() {
        super.tick();
        if (EnderscapeClient.postMirrorUseTicks > 0) EnderscapeClient.postMirrorUseTicks--;
    }
}