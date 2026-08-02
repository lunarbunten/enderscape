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
import net.penumbra.enderscape.manager.ClientsideConstants;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.sound.VoidedLoopSoundInstance;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class VoidNoiseScreenEffect extends EnderscapeHudElement {

    public static final Identifier VOID_VIGNETTE_INVERTED = Enderscape.id("textures/misc/void_vignette_inverted.png");
    public static final Identifier VOID_NOISE_OVERLAY_INVERTED = Enderscape.id("overlay/void_noise_overlay_inverted");

    private float alpha = 0, previousAlpha = 0;

    public Optional<VoidedLoopSoundInstance> soundInstance = Optional.empty();

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
            Texture colors are inverted
         */

        int color = ARGB.color(255, ARGB.linearLerp(lerpedAlpha, 0x000000, 0xFFFFFF));

        graphics.blit(
                RenderPipelines.VIGNETTE,
                VOID_VIGNETTE_INVERTED,
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

        /*
            Noise overlay
            Tiled and animated via sprite rendering
         */

        graphics.blitSprite(
                RenderPipelines.VIGNETTE,
                VOID_NOISE_OVERLAY_INVERTED,
                graphics.guiWidth(),
                graphics.guiHeight(),
                0,
                0,
                0,
                0,
                graphics.guiWidth(),
                graphics.guiHeight(),
                color
        );

        graphics.pose().popMatrix();
    }

    @Override
    public void tick() {
        super.tick();

        previousAlpha = alpha;

        float stareTicksPercentage = variables.endermanAngerTicks > 0 ? variables.endermanAngerTicks-- / (float) ClientsideConstants.ENDERMAN_ANGER_MAX_TICKS : 0.0F;
        float voidTicksPercentage = VoidManager.hasVoidTicks(minecraft.player) ? VoidManager.getVoidTicksPercentage(minecraft.player) : 0.0F;
        float target = Math.min(stareTicksPercentage, 0.65F) + voidTicksPercentage;

        float clampedTarget = Mth.clamp(target, 0.0F, 1.0F);
        float lerpedAlpha = Mth.lerp(0.5F, alpha, clampedTarget);
        
        alpha = Mth.clamp(lerpedAlpha, 0.0F, 1.0F);

        soundInstance.filter(minecraft.getSoundManager()::isActive).ifPresentOrElse(
                (instance) -> instance.setTarget(target),
                () -> {
                    if (target > 0.0F) {
                        soundInstance = Optional.of(new VoidedLoopSoundInstance(minecraft, target, (instance) -> {
                            soundInstance = Optional.empty();
                            instance.remove();
                        }));

                        minecraft.getSoundManager().play(soundInstance.get());
                    }
                }
        );
    }

    @Override
    public void reset() {
        alpha = 0.0F;
        variables.endermanAngerTicks = 0;
        soundInstance.ifPresent(VoidedLoopSoundInstance::onRemove);
    }
}