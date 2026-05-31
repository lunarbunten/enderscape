package net.bunten.enderscape.client.item;

import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.item.component.value.FuelDisplay;
import net.bunten.enderscape.item.component.value.FuelTooltip;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

@Environment(EnvType.CLIENT)
public class FueledToolTooltip implements ClientTooltipComponent {

    private final ItemStack stack;
    private final FuelTooltip tooltip;

    public FueledToolTooltip(ItemStack stack) {
        this.stack = stack;
        this.tooltip = FueledTool.tooltip(stack);
    }

    @Override
    public int getHeight(Font font) {
        return 12 + (int) tooltip.offset().y;
    }

    @Override
    public int getWidth(Font font) {
        return getBarWidth() + (int) tooltip.offset().x;
    }

    private int getBarWidth() {
        return 32 + 11 * (FueledTool.maxFuel(stack) - 2);
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        renderFuelBar(graphics, x, y + 1);
    }

    private void renderFuelBar(GuiGraphicsExtractor graphics, int x, int y) {
        int rx = x;

        Vec2 offset = tooltip.offset();

        int fuel = FueledTool.currentFuel(stack);
        int maxFuel = FueledTool.maxFuel(stack);

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;
            rx += (i == 0) ? 0 : (i == 1) ? 20 : 11;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == 0) ? 20 : (i == maxFuel - 1) ? 12 : 11;
            int offsetY = (i == 0) ? -2 : 0;
            int spriteHeight = (i == 0) ? 10 : 6;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FuelDisplay.segmentOf(tooltip.empty(), index), rx + (int) offset.x, y + offsetY + (int) offset.y, width, spriteHeight);

            if (isFueled) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, tooltip.fueled(), rx + ((i == 0) ? 9 : 0) + (int) offset.x, y + (int) offset.y, 11, 6);
            }
        }
    }
}
