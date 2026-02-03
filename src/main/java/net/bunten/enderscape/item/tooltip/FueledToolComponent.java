package net.bunten.enderscape.item.tooltip;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record FueledToolComponent(ItemStack stack) implements TooltipComponent {
}