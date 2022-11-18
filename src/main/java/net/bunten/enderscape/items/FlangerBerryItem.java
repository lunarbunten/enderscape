package net.bunten.enderscape.items;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FlangerBerryItem extends AbstractVineItem {

    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationMod(0.8F).build();

    public FlangerBerryItem(Item.Properties settings) {
        super(EnderscapeBlocks.FLANGER_BERRY_VINE, settings.food(FOOD_PROPERTIES));
    }
}