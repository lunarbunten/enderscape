package net.bunten.enderscape.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.MirrorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class MirrorHud extends DrawableHelper {

    private final MinecraftClient client = MinecraftClient.getInstance();

    private double heightOffset = 0;
    private double totalAlpha = 0;

    private double highlightAlpha = 0;
    private int highlightDisplayTicks = 0;

    private double outlineAlpha = 0;
    private int outlineDisplayTicks = 0;
    
    private int energy;
    private int maxEnergy;
    private int teleportCost;

    private int width;
    private int height;

    private void render(MatrixStack matrix, float delta) {
        if (client.player == null || client.options.hudHidden || totalAlpha <= 0 || !client.options.getPerspective().isFirstPerson() || client.player.getAbilities().creativeMode || client.player.isSpectator()) {
            return;
        }

        matrix.push();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, (float) totalAlpha);

        matrix.translate(0, heightOffset, 0);
        
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/gui/mirror.png"));

        int totalLength = (11 * maxEnergy) + 1;

        int x = width / 2 - (totalLength / 2);
        int y = height / 2 - 13;

        /*
            Renders the empty energy bar and adjusts for size
        */
        int x1 = x;
        drawTexture(matrix, x1, y, 0, 0, 11, 5, 64, 32);
        if (maxEnergy > 2) {
            for (int i = 0; i < maxEnergy - 2; i++) {
                drawTexture(matrix, x1 += 11, y, 11, 0, 11, 5, 64, 32);
            }
        }
        drawTexture(matrix, x1 += 11, y, 22, 0, 12, 5, 64, 32);

        /*
            Renders the filled energy bar and adjusts for size
        */  
        int x2 = x;
        if (energy > 0) {
            drawTexture(matrix, x2, y, 0, 5, 12, 5, 64, 32);
            if (energy > 1) {
                var charge = energy == maxEnergy ? energy - 1 : energy;
                for (int i = 1; i < charge; i++) {
                    drawTexture(matrix, x2 += 11, y, 11, 5, 11, 5, 64, 32);
                }
            }
            if (energy == maxEnergy) {
                drawTexture(matrix, x2 += 11, y, 22, 5, 12, 5, 64, 32); 
            }
        }

        /*
            Renders the red outline when teleporting cannot be done
        */
        int x3 = x;
        if (outlineAlpha > 0) {
            RenderSystem.setShaderColor(1, 1, 1, (float) outlineAlpha);
            drawTexture(matrix, x3, y, 0, 15, 11, 5, 64, 32);
            if (maxEnergy > 2) {
                for (int i = 0; i < maxEnergy - 2; i++) {
                    drawTexture(matrix, x3 += 11, y, 11, 15, 11, 5, 64, 32);
                }
            }
            drawTexture(matrix, x3 += 11, y, 22, 15, 12, 5, 64, 32);
        }
        
        /*
            Renders the white energy overlay to display cost
        */
        int x4 = x2 + 11;
        if (highlightAlpha > 0) {

            RenderSystem.setShaderColor(1, 1, 1, (float) highlightAlpha);
            for (int i = 0; i < teleportCost; i++) {
                if (i < energy) {
                    drawTexture(matrix, x4 -= 11, y, 0, 10, 11, 5, 64, 32);
                }
            }
        }

        matrix.pop();
    }

    public void tick() {
        width = client.getWindow().getScaledWidth();
        height = client.getWindow().getScaledHeight();

        ClientPlayerEntity mob = client.player;

        if (mob != null) {
            
            ItemStack stack = MirrorUtil.isMirror(mob.getMainHandStack()) ? mob.getMainHandStack() : mob.getOffHandStack();
            boolean displayUI = MirrorUtil.canDisplayUI(stack, mob) && !mob.isSpectator() && mob.getActiveItem().isEmpty();
            double speedModifier = mob.getActiveItem().isEmpty() ? 1 : 1.8F;

            if (displayUI) {
                energy = MirrorUtil.getEnergy(stack);
                maxEnergy = MirrorUtil.getMaximumEnergy(stack);
                teleportCost = MirrorUtil.getCost(stack, mob, MirrorUtil.getPos(stack));

                totalAlpha = MathUtil.lerp(0.5F, totalAlpha, 1);
                heightOffset = MathUtil.lerp(0.5F, heightOffset, -3);
            } else {
                totalAlpha = MathUtil.lerp(0.5F * speedModifier, totalAlpha, 0);
                heightOffset = MathUtil.lerp(0.5F * speedModifier, heightOffset, 0);
            }

            boolean displayOutline = teleportCost > energy || !MirrorUtil.isSameDimension(stack, mob);

            if (displayUI) {
                if (displayOutline) {
                    float sin = (MathHelper.sin(outlineDisplayTicks * 0.4F) * 0.5F) + 0.5F;
                    sin = MathHelper.clamp(sin, 0.3F, 1);
                    outlineAlpha = MathUtil.lerp(0.5F * speedModifier, outlineAlpha, sin); 
                } else {
                    outlineAlpha = MathUtil.lerp(0.08F * speedModifier, outlineAlpha, 0);
                }
            } else {
                outlineAlpha = MathUtil.lerp(0.3F * speedModifier, outlineAlpha, 0);
            }

            if (displayUI) {
                if (!displayOutline) {
                    float sin = (MathHelper.sin(highlightDisplayTicks * 0.25F) * 0.2F) + 0.5F;
                    sin = MathHelper.clamp(sin, 0.5F, 1);
                    highlightAlpha = MathUtil.lerp(0.5F * speedModifier, highlightAlpha, sin);
                } else {
                    highlightAlpha = 0;
                }
            } else {
                highlightAlpha = MathUtil.lerp(0.3F * speedModifier, highlightAlpha, 0);
            }

            if (highlightAlpha > 0) {
                highlightDisplayTicks++;
            } else {
                highlightDisplayTicks = 0;
            }

            if (outlineAlpha > 0) {
                outlineDisplayTicks++;
            } else {
                outlineDisplayTicks = 0;
            }
        }
    }

    public static void init() {
        final MirrorHud mirrorHud = new MirrorHud();

        HudRenderCallback.EVENT.register(mirrorHud::render);
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                mirrorHud.tick();
            }
        });
    }
}