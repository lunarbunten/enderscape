package net.bunten.enderscape.item.component;

import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.minecraft.world.item.ItemStack;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.ENABLED;

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