package net.penumbra.enderscape.item.component;

import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;

import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.ENABLED;

public record Enabled() {

    public static boolean is(ItemStack stack) {
        return stack.has(ENABLED);
    }

    public static boolean get(ItemStack stack) {
        return stack.getOrDefault(EnderscapeDataComponents.ENABLED, false);
    }

    public static void set(ItemStack stack, boolean value) {
        stack.set(ENABLED, value);
    }
}