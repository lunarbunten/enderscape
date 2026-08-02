package net.penumbra.enderscape.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;

import static net.penumbra.enderscape.manager.DashJumpManager.dashJumpChargedDuration;

public class ClientsideDashJumpManager {

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static final Identifier JUMP_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_background");
    private static final Identifier JUMP_BAR_COOLDOWN_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_cooldown");
    private static final Identifier JUMP_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_progress");

    public static boolean skipRenderingDashJumpBar() {
        LocalPlayer player = minecraft.player;

        return player == null || player.getVehicle() != null || dashJumpChargedDuration(player) <= 0 || !player.getUseItem().has(EnderscapeDataComponents.DASH_JUMP);
    }

    public static void extractDashJumpChargeBar(GuiGraphicsExtractor graphics) {
        LocalPlayer player = minecraft.player;
        ItemStack useItem = player.getUseItem();

        int left = (minecraft.getWindow().getGuiScaledWidth() - 182) / 2;
        int top = minecraft.getWindow().getGuiScaledHeight() - 24 - 5;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                JUMP_BAR_BACKGROUND_SPRITE,
                left,
                top,
                182,
                5
        );

        if (player.getCooldowns().isOnCooldown(useItem) || !DashJumpManager.hasFoodOrIsCreative(player)) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    JUMP_BAR_COOLDOWN_SPRITE,
                    left,
                    top,
                    182,
                    5
            );

        } else {
            int progress = Mth.lerpDiscrete(
                    DashJumpManager.getChargeProgress(player),
                    0,
                    182
            );

            if (progress > 0) {
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        JUMP_BAR_PROGRESS_SPRITE,
                        182,
                        5,
                        0,
                        0,
                        left,
                        top,
                        progress,
                        5
                );
            }
        }
    }
}
