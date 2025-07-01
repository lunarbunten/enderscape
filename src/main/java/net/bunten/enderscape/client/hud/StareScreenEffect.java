package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.EnderscapeClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static net.bunten.enderscape.client.EnderscapeClient.MAX_STARE_STICKS;

public class StareScreenEffect extends HudElement {

    public static final ResourceLocation STATIC_TEXTURE = Enderscape.id("textures/misc/static.png");

    public StareScreenEffect() {
        super(Enderscape.id("stare_screen"), RenderPhase.BEFORE_HUD);
    }

    public void render(GuiGraphics graphics, DeltaTracker delta) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().options.hideGui || !Minecraft.getInstance().options.getCameraType().isFirstPerson() || Minecraft.getInstance().player.isSpectator() || EnderscapeClient.stareTicks <= 0 || !EnderscapeConfig.getInstance().endermanStaticOverlay) {
            return;
        }

        graphics.pose().pushPose();

        float alpha = Mth.clamp((float) EnderscapeClient.stareTicks / MAX_STARE_STICKS, 0.0F, 0.25F);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);

        graphics.setColor(1, 1, 1, alpha);
        graphics.blit(
                STATIC_TEXTURE,
                0,
                0,
                Minecraft.getInstance().player.getRandom().nextInt(graphics.guiWidth() / 2),
                Minecraft.getInstance().player.getRandom().nextInt(graphics.guiHeight() / 2),
                graphics.guiWidth(),
                graphics.guiHeight()
        );

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        graphics.pose().popPose();
    }

    public void tick() {
        super.tick();
        if (EnderscapeClient.stareTicks > 0 && !Minecraft.getInstance().isPaused()) EnderscapeClient.stareTicks--;
    }
}