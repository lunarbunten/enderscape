package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.MirrorContext;
import net.bunten.enderscape.item.MirrorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.util.NineSliceBlitUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NebuliteToolHud extends HudElement {

    public NebuliteToolHud() {
        super(Enderscape.id("nebulite_tool"), RenderPhase.AFTER_HUD);
    }

    private static final ResourceLocation[] EMPTY_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/empty_segments/start"),
            Enderscape.id("nebulite_tool/hud/empty_segments/loop"),
            Enderscape.id("nebulite_tool/hud/empty_segments/end")
    };

    private static final ResourceLocation[] FUELED_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/fueled_segments/start"),
            Enderscape.id("nebulite_tool/hud/fueled_segments/loop"),
            Enderscape.id("nebulite_tool/hud/fueled_segments/end")
    };

    private static final ResourceLocation[] INVALID_OVERLAY_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/start"),
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/loop"),
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/end")
    };

    private static final ResourceLocation TRANSDIMENSIONAL_OUTLINE = Enderscape.id("nebulite_tool/hud/transdimensional_outline");

    private static final ResourceLocation COST_OVERLAY_SEGMENT = Enderscape.id("nebulite_tool/hud/cost_overlay_segment");

    private float heightOffset = 0;
    private float totalAlpha = 0;

    private float costAlpha = 0;
    private int costDisplayTicks = 0;

    private float invalidAlpha = 0;
    private int invalidDisplayTicks = 0;

    private float transdimensionalAlpha = 0;
    private int transdimensionalDisplayTicks = 0;

    private int costOverlayPosition = -1;
    
    private int fuel;
    private int maxFuel;
    private int cost;

    public void render(GuiGraphics graphics, DeltaTracker delta) {
        if (!config.nebuliteToolHudEnabled || Minecraft.getInstance().player == null || Minecraft.getInstance().options.hideGui || totalAlpha <= 0.01F || !Minecraft.getInstance().options.getCameraType().isFirstPerson() || Minecraft.getInstance().player.isSpectator()) {
            return;
        }

        graphics.pose().pushPose();

        graphics.pose().translate(0, heightOffset, 0);

        float opacity = totalAlpha * ((float) (config.nebuliteToolHudOpacity)) / 100.0F;

        int x = graphics.guiWidth() / 2 - (((11 * maxFuel) + 1) / 2);
        int y = graphics.guiHeight() / 2 - 13 - config.nebuliteToolHudOffset;

        renderFuelBar(graphics, x, y, opacity);
        renderTransdimensionalOutline(graphics, x, y, opacity);
        renderInvalidOverlay(graphics, x, y, opacity);
        renderCostOverlay(graphics, y, opacity);

        graphics.pose().popPose();
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y, float opacity) {
        int lastFueled = -1;
        int rx = x;

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;

            if (i > 0) rx += 11;
            if (isFueled) lastFueled = rx;

            ResourceLocation[] segments = isFueled ? FUELED_SEGMENTS : EMPTY_SEGMENTS;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == maxFuel - 1) ? 12 : 11;

            TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(segments[index]);

            graphics.blit(rx, y, 0, width, 5, sprite, 1.0F, 1.0F, 1.0F, opacity);
        }

        costOverlayPosition = lastFueled;
    }

    private void renderTransdimensionalOutline(GuiGraphics graphics, int x, int y, float opacity) {
        NineSliceBlitUtil nineSliceBlit = new NineSliceBlitUtil(Minecraft.getInstance().getGuiSprites(), graphics, transdimensionalAlpha * opacity);
        nineSliceBlit.blitSprite(TRANSDIMENSIONAL_OUTLINE, x - 6, y - 6, 0, (maxFuel * 11) + 13, 5 + 12);
    }

    private void renderInvalidOverlay(GuiGraphics graphics, int x, int y, float opacity) {
        int rx = x;

        for (int i = 0; i < maxFuel; i++) {
            if (i > 0) rx += 11;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == maxFuel - 1) ? 12 : 11;

            TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(INVALID_OVERLAY_SEGMENTS[index]);
            graphics.blit(rx, y, 0, width, 5, sprite, 1.0F, 1.0F, 1.0F, invalidAlpha * opacity);
        }
    }

    private void renderCostOverlay(GuiGraphics graphics, int y, float opacity) {
        if (costAlpha > 0 && costOverlayPosition >= 0 && fuel >= cost) {
            for (int i = 0; i < cost; i++) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(COST_OVERLAY_SEGMENT);
                graphics.blit(costOverlayPosition - (i * 11), y, 0, 11, 5, sprite, 1.0F, 1.0F, 1.0F, costAlpha * opacity);
            }
        }
    }

    public void tick() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = NebuliteToolItem.is(player.getMainHandItem()) ? player.getMainHandItem() : player.getOffhandItem();
        NebuliteToolContext context = new NebuliteToolContext(stack, player.level(), player);


        boolean displayUI = NebuliteToolItem.is(stack) && context.item().displayHudWhen(context) && !player.isSpectator() && player.getUseItem().isEmpty();
        boolean displayOutline = NebuliteToolItem.is(stack) && !context.item().hideInvalidOutlineWhen(context);
        boolean displayTransdimensional = false;

        if (MirrorItem.is(stack)) {
            MirrorContext mirrorContext = MirrorContext.of(context);
            displayTransdimensional = MirrorItem.hasTransdimensional(mirrorContext) && !MirrorItem.isSameDimension(mirrorContext, mirrorContext.linkedDimension()) && !displayOutline;
        }

        if (displayUI) {
            fuel = NebuliteToolItem.currentFuel(stack);
            maxFuel = NebuliteToolItem.maxFuel(stack);
            cost = NebuliteToolItem.of(stack).fuelCost(context);

            totalAlpha = Mth.lerp(0.5F, totalAlpha, 1);
            heightOffset = Mth.lerp(0.5F, heightOffset, -3);
        } else {
            totalAlpha = Mth.lerp(0.5F, totalAlpha, 0);
            heightOffset = Mth.lerp(0.5F, heightOffset, 0);
        }

        invalidAlpha = Mth.lerp(0.35F, invalidAlpha, (displayUI && displayOutline) ? Mth.clamp((Mth.sin(invalidDisplayTicks * 0.4F) * 0.5F) + 0.5F, 0.75F, 1) : 0);
        costAlpha = Mth.lerp(0.35F, costAlpha, (displayUI && !displayOutline) ? Mth.clamp((Mth.sin(costDisplayTicks * 0.25F) * 0.2F) + 0.5F, 0.75F, 1) : 0);
        transdimensionalAlpha = Mth.lerp(0.35F, transdimensionalAlpha, (displayUI && displayTransdimensional) ? Mth.clamp((Mth.sin(transdimensionalDisplayTicks * 0.1F) * 0.5F) + 0.5F, 0.4F, 1) : 0);

        invalidDisplayTicks = invalidAlpha > 0 ? invalidDisplayTicks + 1 : 0;
        costDisplayTicks = costAlpha > 0 ? costDisplayTicks + 1 : 0;
        transdimensionalDisplayTicks = transdimensionalAlpha > 0 ? transdimensionalDisplayTicks + 1 : 0;
    }
}