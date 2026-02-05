package net.bunten.enderscape.item.tooltip;

import net.bunten.enderscape.item.component.FueledTool;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record FueledToolComponent(ItemStack stack) implements TooltipComponent {

    public static boolean applies(ItemStack stack) {
        if (!FueledTool.is(stack) || !FueledTool.tooltip(stack).visible()) return false;
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
    }
}