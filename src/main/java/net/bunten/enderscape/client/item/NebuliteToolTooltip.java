package net.bunten.enderscape.client.item;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NebuliteToolTooltip implements ClientTooltipComponent {

    private static final ResourceLocation[] EMPTY_SEGMENTS = {
            Enderscape.id("nebulite_tool/tooltip/empty_segments/start"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/loop"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/end")
    };

    private static final ResourceLocation FUELED_SEGMENT = Enderscape.id("nebulite_tool/tooltip/fueled_segment");

    private final ItemStack stack;

    public NebuliteToolTooltip(ItemStack stack) {
        this.stack = stack;

        if (!(stack.getItem() instanceof NebuliteToolItem)) {
            throw new IllegalStateException("Item is not an instance of " + NebuliteToolItem.class + ", unapplicable for " + NebuliteToolTooltip.class);
        }
    }

    @Override
    public int getHeight(Font font) {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return getBarWidth();
    }

    private int getBarWidth() {
        return 32 + 11 * (NebuliteToolItem.maxFuel(stack) - 2);
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    @Override
    public void renderImage(Font font, int x, int y, int k, int l, GuiGraphics graphics) {
        renderFuelBar(graphics, x, y + 1);
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y) {
        int rx = x;

        int fuel = NebuliteToolItem.currentFuel(stack);
        int maxFuel = NebuliteToolItem.maxFuel(stack);

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;
            rx += (i == 0) ? 0 : (i == 1) ? 20 : 11;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == 0) ? 20 : (i == maxFuel - 1) ? 12 : 11;
            int offsetY = (i == 0) ? -2 : 0;
            int spriteHeight = (i == 0) ? 10 : 6;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EMPTY_SEGMENTS[index], rx, y + offsetY, width, spriteHeight);

            if (isFueled) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FUELED_SEGMENT, rx + ((i == 0) ? 9 : 0), y, 11, 6);
            }
        }

    }
}
