package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.FueledTool;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.LodestoneTeleporter;
import net.bunten.enderscape.item.LodestoneTrackerContext;
import net.bunten.enderscape.util.NineSliceBlitUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.ENABLED;

@OnlyIn(Dist.CLIENT)
public class FueledToolHud extends HudElement {

    public FueledToolHud() {
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

    private float heightOffset = 0, previousHeightOffset = 0;
    private float totalAlpha = 0, previousTotalAlpha = 0;
    private float invalidAlpha = 0, previousInvalidAlpha = 0;
    private float transdimensionalAlpha = 0, previousTransdimensionalAlpha = 0;
    private float costAlpha = 0, previousCostAlpha = 0;

    private int costDisplayTicks = 0, invalidDisplayTicks = 0, transdimensionalDisplayTicks = 0;

    private int costOverlayPosition = -1;

    private int fuel;
    private int maxFuel;
    private int cost;

    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (!config.nebuliteToolHudEnabled || client.player == null || client.options.hideGui || !client.options.getCameraType().isFirstPerson() || client.player.isSpectator()) {
            return;
        }

        float delta = tracker.getGameTimeDeltaPartialTick(false);
        float offset = Mth.lerp(delta, previousHeightOffset, heightOffset);
        float total = Mth.lerp(delta, previousTotalAlpha, totalAlpha);

        if (total <= 0.01F) return;

        graphics.pose().pushPose();

        int verticalOffset = config.nebuliteToolHudOffset;
        graphics.pose().translate(0.0F, verticalOffset < 0 ? -offset : offset, 0.0F);

        float opacity = Mth.clamp(total * ((float) (config.nebuliteToolHudOpacity)) / 100.0F, 0.0F, 1.0F);

        int x = graphics.guiWidth() / 2 - (((11 * maxFuel) + 1) / 2);
        int y = graphics.guiHeight() / 2 - verticalOffset;

        renderFuelBar(graphics, x, y, opacity);
        renderTransdimensionalOutline(graphics, x, y, Mth.lerp(delta, previousTransdimensionalAlpha, transdimensionalAlpha) * opacity);
        renderInvalidOverlay(graphics, x, y, Mth.lerp(delta, previousInvalidAlpha, invalidAlpha)  * opacity);
        renderCostOverlay(graphics, y, Mth.lerp(delta, previousCostAlpha, costAlpha) * opacity);

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
        Player player = client.player;
        if (player == null) return;

        previousTotalAlpha = totalAlpha;
        previousHeightOffset = heightOffset;
        previousCostAlpha = costAlpha;
        previousInvalidAlpha = invalidAlpha;
        previousTransdimensionalAlpha = transdimensionalAlpha;

        ItemStack stack = FueledTool.is(player.getMainHandItem()) ? player.getMainHandItem() : player.getOffhandItem();
        ItemStackContext context = new ItemStackContext(stack, player.level(), player);

        boolean displayUI = FueledTool.is(stack) && context.item().displayHudWhen(context) && !player.isSpectator() && player.getUseItem().isEmpty();
        boolean displayOutline = false;
        boolean displayTransdimensional = false;

        if (stack.has(ENABLED)) {
            displayUI = displayUI && stack.get(ENABLED);
        }

        if (LodestoneTeleporter.is(stack)) {
            LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

            displayUI = displayUI && LodestoneTeleporter.isLinked(stack);
            displayOutline = !LodestoneTeleporter.hideInvalidOutlineWhen1(context);
            displayTransdimensional = LodestoneTeleporter.isTransdimensionalAllowed(tracker) && !LodestoneTeleporter.isSameDimension(tracker, tracker.linkedDimension()) && !displayOutline;
        }

        if (displayUI) {
            fuel = FueledTool.currentFuel(stack);
            maxFuel = FueledTool.maxFuel(stack);
            cost = FueledTool.fuelCost(context);

            totalAlpha = Mth.lerp(0.5F, totalAlpha, 1);
            heightOffset = Mth.lerp(0.5F, heightOffset, -3);
        } else {
            totalAlpha = Mth.lerp(0.5F, totalAlpha, 0);
            heightOffset = Mth.lerp(0.5F, heightOffset, 0);
        }

        invalidAlpha = Mth.lerp(0.35F, invalidAlpha, (displayUI && displayOutline) ? Mth.clamp((Mth.sin(invalidDisplayTicks * 0.4F) * 0.5F) + 0.5F, 0.65F, 1) : 0);
        costAlpha = Mth.lerp(0.35F, costAlpha, (displayUI && !displayOutline) ? Mth.clamp((Mth.sin(costDisplayTicks * 0.25F) * 0.2F) + 0.5F, 0.75F, 1) : 0);
        transdimensionalAlpha = Mth.lerp(0.35F, transdimensionalAlpha, (displayUI && displayTransdimensional) ? Mth.clamp((Mth.sin(transdimensionalDisplayTicks * 0.1F) * 0.5F) + 0.5F, 0.4F, 1) : 0);

        invalidDisplayTicks = invalidAlpha > 0 ? invalidDisplayTicks + 1 : 0;
        costDisplayTicks = costAlpha > 0 ? costDisplayTicks + 1 : 0;
        transdimensionalDisplayTicks = transdimensionalAlpha > 0 ? transdimensionalDisplayTicks + 1 : 0;
    }
}