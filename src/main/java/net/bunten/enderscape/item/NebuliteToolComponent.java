package net.bunten.enderscape.item;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record NebuliteToolComponent(ItemStack stack) implements TooltipComponent {
}