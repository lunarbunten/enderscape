package net.penumbra.enderscape.item.tooltip;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.penumbra.enderscape.item.component.FueledTool;

import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.CURRENT_FUEL;
import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.FUELED_TOOL;

public record FueledToolComponent(ItemStack stack) implements TooltipComponent {

    public static boolean applies(ItemStack stack) {
        if (!FueledTool.is(stack) || !FueledTool.tooltip(stack).visible()) return false;

        TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        return display.shows(CURRENT_FUEL) && display.shows(FUELED_TOOL);
    }
}