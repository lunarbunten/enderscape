package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class RubbleShieldItem extends ShieldItem {
    public RubbleShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack2) {
        return stack2.is(EnderscapeItemTags.REPAIRS_RUBBLE_SHIELDS);
    }
}