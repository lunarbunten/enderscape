package net.bunten.enderscape.items;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.item.BlockItem;

public class MurushroomsItem extends BlockItem {
    public MurushroomsItem(Settings settings) {
        super(EnderscapeBlocks.MURUSHROOMS, settings.food(EnderscapeItems.MURUSHROOMS_COMPONENT));
    }
}