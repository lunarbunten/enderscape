package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.items.ChargedUsageContext;
import net.bunten.enderscape.items.NebuliteChargedItem;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NebuliteChargedItemHud extends HudElement {
    
    public NebuliteChargedItemHud() {
        super(RenderPhase.AFTER_HUD);
    }

    private double heightOffset = 0;
    private double totalAlpha = 0;

    private double highlightAlpha = 0;
    private int highlightDisplayTicks = 0;

    private double outlineAlpha = 0;
    private int outlineDisplayTicks = 0;
    
    private int energy;
    private int maxEnergy;
    private int cost;

    public void render(PoseStack matrix, float delta) {
        if (!Config.CLIENT.displayChargedHud() || client.player == null || client.options.hideGui || totalAlpha <= 0 || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator()) {
            return;
        }

        matrix.pushPose();

        float opacity = ((float) (Config.CLIENT.chargedHudOpacity())) / 100;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, (float) totalAlpha * opacity);

        matrix.translate(0, heightOffset, 0);
        
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/gui/nebulite_energy.png"));

        int totalLength = (11 * maxEnergy) + 1;

        int x = width / 2 - (totalLength / 2);
        int y = height / 2 - 13 - Config.CLIENT.chargedHudOffset();

        /*
            Renders the empty energy bar and adjusts for size
        */
        int x1 = x;
        blit(matrix, x1, y, 0, 0, 11, 5, 64, 32);
        if (maxEnergy > 2) {
            for (int i = 0; i < maxEnergy - 2; i++) {
                blit(matrix, x1 += 11, y, 11, 0, 11, 5, 64, 32);
            }
        }
        blit(matrix, x1 += 11, y, 22, 0, 12, 5, 64, 32);

        /*
            Renders the filled energy bar and adjusts for size
        */  
        int x2 = x;
        if (energy > 0) {
            blit(matrix, x2, y, 0, 5, 12, 5, 64, 32);
            if (energy > 1) {
                var charge = energy == maxEnergy ? energy - 1 : energy;
                for (int i = 1; i < charge; i++) {
                    blit(matrix, x2 += 11, y, 11, 5, 11, 5, 64, 32);
                }
            }
            if (energy == maxEnergy) {
                blit(matrix, x2 += 11, y, 22, 5, 12, 5, 64, 32); 
            }
        }

        /*
            Renders the red outline when you cannot use the item
        */
        int x3 = x;
        if (outlineAlpha > 0) {
            RenderSystem.setShaderColor(1, 1, 1, (float) outlineAlpha * opacity);
            blit(matrix, x3, y, 0, 15, 11, 5, 64, 32);
            if (maxEnergy > 2) {
                for (int i = 0; i < maxEnergy - 2; i++) {
                    blit(matrix, x3 += 11, y, 11, 15, 11, 5, 64, 32);
                }
            }
            blit(matrix, x3 += 11, y, 22, 15, 12, 5, 64, 32);
        }
        
        /*
            Renders the white energy overlay to display cost
        */
        int x4 = x2 + 11;
        if (highlightAlpha > 0) {

            RenderSystem.setShaderColor(1, 1, 1, (float) highlightAlpha * opacity);
            for (int i = 0; i < cost; i++) {
                if (i < energy) {
                    blit(matrix, x4 -= 11, y, 0, 10, 11, 5, 64, 32);
                }
            }
        }

        matrix.popPose();
    }

    public void tick() {
        super.tick();

        Player player = client.player;
        if (player != null) {

            ItemStack stack = NebuliteChargedItem.is(player.getMainHandItem()) ? player.getMainHandItem() : player.getOffhandItem();
            ChargedUsageContext context = new ChargedUsageContext(stack, player.getLevel(), player);
            boolean displayUI = NebuliteChargedItem.is(stack) ? context.getChargedItem().showUI(stack, player) : false;
            double speedModifier = player.getUseItem().isEmpty() ? 1 : 1.8F;

            if (displayUI) {
                energy = NebuliteChargedItem.getEnergy(stack);
                maxEnergy = context.getChargedItem().getMaximumEnergy(stack);
                cost = context.getChargedItem().getUseCost(context);

                totalAlpha = MathUtil.lerp(0.5F, totalAlpha, 1);
                heightOffset = MathUtil.lerp(0.5F, heightOffset, -3);
            } else {
                totalAlpha = MathUtil.lerp(0.5F * speedModifier, totalAlpha, 0);
                heightOffset = MathUtil.lerp(0.5F * speedModifier, heightOffset, 0);
            }

            boolean displayOutline = cost > energy;

            if (displayUI) {
                if (displayOutline) {
                    float sin = (MathUtil.sin(outlineDisplayTicks * 0.4F) * 0.5F) + 0.5F;
                    sin = MathUtil.clamp(sin, 0.3F, 1);
                    outlineAlpha = MathUtil.lerp(0.5F * speedModifier, outlineAlpha, sin); 
                } else {
                    outlineAlpha = MathUtil.lerp(0.08F * speedModifier, outlineAlpha, 0);
                }
            } else {
                outlineAlpha = MathUtil.lerp(0.3F * speedModifier, outlineAlpha, 0);
            }

            if (displayUI) {
                if (!displayOutline) {
                    float sin = (MathUtil.sin(highlightDisplayTicks * 0.25F) * 0.2F) + 0.5F;
                    sin = MathUtil.clamp(sin, 0.5F, 1);
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
}