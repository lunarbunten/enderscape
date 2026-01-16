package net.bunten.enderscape.item.tooltip;

import net.bunten.enderscape.item.component.FueledTool;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.CURRENT_FUEL;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.FUELED_TOOL;

public record FueledToolComponent(ItemStack stack) implements TooltipComponent {

    public static boolean applies(ItemStack stack) {
        if (!FueledTool.is(stack) || !FueledTool.tooltip(stack).visible()) return false;

        TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        return display.shows(CURRENT_FUEL) && display.shows(FUELED_TOOL);
    }
}