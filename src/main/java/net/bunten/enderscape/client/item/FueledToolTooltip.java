package net.bunten.enderscape.client.item;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.FueledTool;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FueledToolTooltip implements ClientTooltipComponent {

    private static final ResourceLocation[] EMPTY_SEGMENTS = {
            Enderscape.id("nebulite_tool/tooltip/empty_segments/start"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/loop"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/end")
    };

    private static final ResourceLocation FUELED_SEGMENT = Enderscape.id("nebulite_tool/tooltip/fueled_segment");

    private final ItemStack stack;

    public FueledToolTooltip(ItemStack stack) {
        this.stack = stack;

        if (!(stack.getItem() instanceof FueledTool)) {
            throw new IllegalStateException("Item is not an instance of " + FueledTool.class + ", unapplicable for " + FueledToolTooltip.class);
        }
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return getBarWidth();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        renderFuelBar(graphics, x, y + 1);
    }

    private int getBarWidth() {
        return 32 + 11 * (FueledTool.maxFuel(stack) - 2);
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y) {
        int rx = x;

        int fuel = FueledTool.currentFuel(stack);
        int maxFuel = FueledTool.maxFuel(stack);

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;
            rx += (i == 0) ? 0 : (i == 1) ? 20 : 11;

            int index = (i == 0) ? 0 : (i == maxFuel - 1) ? 2 : 1;
            int width = (i == 0) ? 20 : (i == maxFuel - 1) ? 12 : 11;
            int offsetY = (i == 0) ? -2 : 0;
            int spriteHeight = (i == 0) ? 10 : 6;

            graphics.blitSprite(EMPTY_SEGMENTS[index], rx, y + offsetY, width, spriteHeight);

            if (isFueled) {
                graphics.blitSprite(FUELED_SEGMENT, rx + ((i == 0) ? 9 : 0), y, 11, 6);
            }
        }
    }
}
