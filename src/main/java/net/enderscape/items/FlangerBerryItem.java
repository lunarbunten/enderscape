package net.enderscape.items;

import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndFoods;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class FlangerBerryItem extends BlockItem {
    public FlangerBerryItem(Item.Settings settings) {
        super(EndBlocks.FLANGER_BERRY_VINE, settings.food(EndFoods.FLANGER_BERRY));
    }

    public String getTranslationKey() {
        return getOrCreateTranslationKey();
    }
}