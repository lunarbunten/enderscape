package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.LodestoneTrackerContext;
import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.item.component.LodestoneTeleportation;
import net.bunten.enderscape.item.component.value.FuelDisplay;
import net.bunten.enderscape.item.component.value.FuelHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.ENABLED;

@Environment(EnvType.CLIENT)
public class FueledToolHud extends HudElement {

    public FueledToolHud() {
        super(RenderPhase.AFTER_HUD);
    }

    private ResourceLocation emptySegments;
    private ResourceLocation fueledSegments;
    private ResourceLocation invalidSegments;
    private ResourceLocation outlineOverlay;
    private ResourceLocation costOverlay;

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

        graphics.pose().pushMatrix();

        int verticalOffset = config.nebuliteToolHudOffset;
        graphics.pose().translate(0.0F, verticalOffset < 0 ? -offset : offset);

        float opacity = Mth.clamp(total * ((float) (config.nebuliteToolHudOpacity)) / 100.0F, 0.0F, 1.0F);

        int x = graphics.guiWidth() / 2 - (((11 * maxFuel) + 1) / 2);
        int y = graphics.guiHeight() / 2 - verticalOffset;

        renderFuelBar(graphics, x, y, opacity);
        renderTransdimensionalOutline(graphics, x, y, Mth.lerp(delta, previousTransdimensionalAlpha, transdimensionalAlpha) * opacity);
        renderInvalidOverlay(graphics, x, y, Mth.lerp(delta, previousInvalidAlpha, invalidAlpha)  * opacity);
        renderCostOverlay(graphics, y, Mth.lerp(delta, previousCostAlpha, costAlpha) * opacity);

        graphics.pose().popMatrix();
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y, float opacity) {
        int lastFueled = -1;
        int rx = x;

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;

            if (i > 0) rx += 11;
            if (isFueled) lastFueled = rx;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == maxFuel - 1) ? 12 : 11;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FuelDisplay.segmentOf(isFueled ? fueledSegments : emptySegments, index), rx, y, width, 5, white(opacity));
        }

        costOverlayPosition = lastFueled;
    }

    private void renderTransdimensionalOutline(GuiGraphics graphics, int x, int y, float opacity) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, outlineOverlay, x - 6, y - 6, (maxFuel * 11) + 13, 5 + 12, white(opacity));
    }

    private void renderInvalidOverlay(GuiGraphics graphics, int x, int y, float opacity) {
        int rx = x;

        for (int i = 0; i < maxFuel; i++) {
            if (i > 0) rx += 11;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == maxFuel - 1) ? 12 : 11;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FuelDisplay.segmentOf(invalidSegments, index), rx, y, width, 5, white(opacity));
        }
    }

    private void renderCostOverlay(GuiGraphics graphics, int y, float opacity) {
        if (opacity > 0 && costOverlayPosition >= 0 && fuel >= cost) {
            for (int i = 0; i < cost; i++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, costOverlay, costOverlayPosition - (i * 11), y, 11, 5, white(opacity));
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

        boolean displayUI = FueledTool.is(stack) && FueledTool.get(stack).display().hud().visible() && !player.isSpectator() && player.getUseItem().isEmpty();
        boolean displayOutline = false;
        boolean displayTransdimensional = false;

        if (stack.has(ENABLED)) {
            displayUI = displayUI && stack.get(ENABLED);
        }

        if (LodestoneTeleportation.is(stack)) {
            LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

            displayUI = displayUI && LodestoneTeleportation.isLinked(stack);
            displayOutline = !LodestoneTeleportation.hideInvalidOutlineWhen(context);
            displayTransdimensional = LodestoneTeleportation.isTransdimensionalAllowed(tracker) && !LodestoneTeleportation.isSameDimension(tracker, tracker.linkedDimension()) && !displayOutline;
        }

        if (displayUI) {
            FuelHud hud = FueledTool.hud(stack);

            emptySegments = hud.empty();
            fueledSegments = hud.fueled();
            invalidSegments = hud.invalidOverlay();
            outlineOverlay = hud.outline();
            costOverlay = hud.costOverlay();

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